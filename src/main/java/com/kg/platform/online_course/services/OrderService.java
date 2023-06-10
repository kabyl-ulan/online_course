package com.kg.platform.online_course.services;

import com.kg.platform.online_course.dto.request.OrderRequest;
import com.kg.platform.online_course.dto.request.OrderRequestFromCart;
import com.kg.platform.online_course.dto.response.OrderDetailsResponse;
import com.kg.platform.online_course.dto.response.OrderResponse;
import com.kg.platform.online_course.exceptions.ECommerceException;
import com.kg.platform.online_course.exceptions.NotFoundException;
import com.kg.platform.online_course.mappers.OrderItemMapper;
import com.kg.platform.online_course.mappers.OrderMapper;
import com.kg.platform.online_course.models.Order;
import com.kg.platform.online_course.models.OrderItem;
import com.kg.platform.online_course.models.Product;
import com.kg.platform.online_course.models.User;
import com.kg.platform.online_course.models.enums.OrderStatus;
import com.kg.platform.online_course.repositories.OrderItemRepository;
import com.kg.platform.online_course.repositories.OrderRepository;
import com.kg.platform.online_course.repositories.ProductRepository;
import com.kg.platform.online_course.util.InvoiceGenerator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

@Transactional
@Service
@Slf4j
@AllArgsConstructor
public class OrderService {
    private UserService userService;
    private OrderRepository orderRepository;
    private OrderItemRepository orderItemRepository;
    private ProductRepository productRepository;

    private CartService cartService;
    private OrderMapper orderMapper;
    private OrderItemMapper orderItemMapper;

    private EmailService emailService;
    private InvoiceGenerator invoiceGenerator;

    public Page<OrderResponse> getAllOrders(Pageable pageable) {
        return orderRepository.findAll(pageable)
                .map(orderMapper::toOrderResponse);
    }

    public List<OrderResponse> getUserOrders(Principal principal) {
        return orderRepository.findAllByUserEmail(principal.getName())
                .stream()
                .map(orderMapper::toOrderResponse)
                .toList();
    }

    public OrderDetailsResponse getOrderById(Long orderId) {
        return orderMapper.toOrderDetailsResponse(findById(orderId));
    }

    public Order findById(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(NotFoundException::new);
        order.setOrderItems(orderItemRepository.findByOrder(order));
        return order;
    }

    private Order saveOrder(User user, List<OrderItem> orderItems) {
        Order order = Order.builder()
                .user(user)
                .orderStatus(OrderStatus.AWAITING)
                .orderDate(LocalDate.now())
                .orderItems(orderItems)
                .totalPrice(orderItems.stream()
                        .mapToDouble(OrderItem::getTotalPrice)
                        .sum())
                .build();

        orderItems.forEach(orderItem -> orderItem.setOrder(order));

        return orderRepository.save(order);
    }


    @Async
    public void placeOrder(Principal principal, OrderRequestFromCart orderRequest) {
        User user = userService.findUserByEmail(principal.getName());

        List<OrderItem> orderItems = orderRequest.getCartItemIds().stream()
                .map(id -> cartService.getById(id))
                .map(orderItemMapper::toOrderItem)
                .toList();
        saveOrder(user, orderItems);
        cartService.clearCart(principal);
    }


    public OrderDetailsResponse createOrder(OrderRequest orderRequest, Principal principal) {
        User user = userService.findUserByEmail(principal.getName());

        List<OrderItem> orderItems = orderRequest.getOrderItems().stream()
                .map(orderItem -> {
                    Product product = productRepository.findById(orderItem.getProductId()).orElseThrow(NotFoundException::new);
                    if (product.getAmount() < orderItem.getQuantity())
                        throw new ECommerceException("Not enough product in stock");
                    return OrderItem.builder()
                            .product(product)
                            .quantity(orderItem.getQuantity())
                            .totalPrice(product.getPrice() * orderItem.getQuantity())
                            .build();
                }).toList();


        return orderMapper.toOrderDetailsResponse( saveOrder(user, orderItems));


    }

    @Async
    public void confirmOrder(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(NotFoundException::new);
        OrderStatus status = order.getOrderStatus();
        if (status == OrderStatus.CONFIRMED || status == OrderStatus.CANCELED || status == OrderStatus.COMPLETED)
            throw new ECommerceException("Order already confirmed or  canceled");

        soldProducts(order.getOrderItems(),true);
        order.setOrderStatus(OrderStatus.CONFIRMED);
        orderRepository.save(order);


//        emailService.sendEmail(order.getUser().getEmail(), "Order confirmation", "Your order confirmed successfully");

    }

    public void deleteOrder(Long orderId) {
        orderRepository.deleteById(orderId);
    }

    @Async
    public void cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(NotFoundException::new);

        if (order.getOrderStatus() == OrderStatus.COMPLETED || order.getOrderStatus() == OrderStatus.CANCELED)
            throw new ECommerceException("Order already completed or canceled");
        soldProducts(order.getOrderItems(),false);
        order.setOrderStatus(OrderStatus.CANCELED);

        orderRepository.save(order);
//        emailService.sendEmail(order.getUser().getEmail(), "Order cancellation", "Your order canceled successfully");
    }

    public void completeOrder(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(NotFoundException::new);
        if (order.getOrderStatus() == OrderStatus.COMPLETED || order.getOrderStatus() == OrderStatus.CANCELED)
            throw new ECommerceException("Order already completed or canceled");

        order.setOrderStatus(OrderStatus.COMPLETED);
        orderRepository.save(order);

    }


    public ResponseEntity<byte[]> generateInvoice(Long orderId) {
        Order order = this.findById(orderId);
        byte[] pdfBytes = invoiceGenerator.generateInvoice(order);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "order-invoice.pdf");

        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }


    @Transactional
    public void soldProducts(List<OrderItem> orderItems, boolean toSell) {
        for (OrderItem orderItem : orderItems) {
            Product product = orderItem.getProduct();

            int quantity = orderItem.getQuantity();
            int sold = product.getSold();
            int amount = product.getAmount();

            if (toSell) {
                product.setAmount(amount - quantity);
                product.setSold(sold + quantity);
            } else {
                product.setAmount(amount + quantity);
                product.setSold(sold - quantity);
            }

        }

    }

}




