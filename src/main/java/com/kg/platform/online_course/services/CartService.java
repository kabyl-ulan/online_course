package com.kg.platform.online_course.services;

import jakarta.transaction.Transactional;
import com.kg.platform.online_course.dto.response.CartItemResponse;
import com.kg.platform.online_course.dto.response.SimpleResponse;
import com.kg.platform.online_course.exceptions.ECommerceException;
import com.kg.platform.online_course.exceptions.NotFoundException;
import com.kg.platform.online_course.mappers.CartItemMapper;
import com.kg.platform.online_course.mappers.CourseMapper;
import com.kg.platform.online_course.models.Cart;
import com.kg.platform.online_course.models.CartItem;
import com.kg.platform.online_course.repositories.CartItemRepository;
import com.kg.platform.online_course.repositories.CartRepository;
import com.kg.platform.online_course.repositories.CourseRepository;
import com.kg.platform.online_course.repositories.UserRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class CartService {

    private final UserRepository userRepository;
    private final CartItemRepository itemRepository;
    private final CourseRepository productRepo;
    private final CartRepository cartRepository;

    private final CourseMapper responseMapper;
    private final CartItemMapper cartItemMapper;

    public CartItem getById(Long id) {
        return itemRepository.findById(id).orElseThrow(NoClassDefFoundError::new);
    }

    public CartItemResponse addToCart(Long productId, Principal principal) {
        Cart cart = cartRepository.findByUserEmail(principal.getName());

        if (cart ==null || !cartRepository.existsById(cart.getId())) {
            cart = Cart.builder().user(userRepository.findByEmail(principal.getName()).get()).build();
            cartRepository.save(cart);
        }

        CartItem cartItem = null;
        if (cartItem != null && cartItem.getCart() == cart) {
            throw new ECommerceException("This product already add!");
        }
        cartItem = CartItem.builder()
                .cart(cart)
                .course(productRepo.findById(productId).orElseThrow())
                .quantity(1)
                .build();
        itemRepository.save(cartItem);
        return cartItemMapper.toCartItemResponse(cartItem);
    }

    public SimpleResponse changeQuantity(Long cartItemId, Integer quantity) {
        CartItem cartItem = itemRepository.findById(cartItemId).orElseThrow(NotFoundException::new);

        cartItem.setQuantity(quantity);
        itemRepository.saveAndFlush(cartItem);
        return new SimpleResponse("Successfully changed", "CHANGE");
    }


    public List<CartItemResponse> getCartItems(Principal principal) {
        List<CartItem> cartItems = itemRepository.findByCartOrderById(cartRepository.findByUserEmail(principal.getName()));
        return cartItems.stream()
                .map(cartItemMapper::toCartItemResponse)
                .toList();
    }

    public SimpleResponse deleteItemFromCart(Long cartItemId){
        itemRepository.deleteById(cartItemId);
        return new SimpleResponse("The cart successfully deleted", "DELETE");
    }
    public void clearCart(Principal principal) {
        Cart cart = cartRepository.findByUserEmail(principal.getName());
        cart.getCartItems().clear();

    }
}
