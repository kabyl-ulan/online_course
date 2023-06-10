package com.kg.platform.online_course.services;

import com.kg.platform.online_course.dto.request.ReviewRequest;
import com.kg.platform.online_course.dto.response.ReviewResponse;
import com.kg.platform.online_course.models.Product;
import com.kg.platform.online_course.models.Review;
import com.kg.platform.online_course.models.User;
import com.kg.platform.online_course.repositories.ProductRepository;
import com.kg.platform.online_course.repositories.ReviewRepository;
import com.kg.platform.online_course.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ReviewService {
    private final ProductRepository productRepository;
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;

    public void createFeedback(ReviewRequest request, Principal principal) {
        Optional<Product> product = productRepository.findById(request.getProductId());
        Optional<User> user = userRepository.findByEmail(principal.getName());
        reviewRepository.save(Review.builder()
                .text(request.getText())
                .date(LocalDate.now())
                .user(user.orElseThrow())
                .product(product.orElseThrow())
                .build());
    }

    public List<ReviewResponse> delete(Long id) {
        Review review = reviewRepository.findById(id).orElseThrow();
        reviewRepository.delete(review);
        return reviewRepository.findAllByProductId(review.getProduct().getId()).stream().map(r -> new ReviewResponse(r.getId(), r.getText(),
                r.getUser().getName() + " " + r.getUser().getSurname(), r.getDate())).collect(Collectors.toList());
    }
}
