package com.kg.platform.online_course.api_controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.kg.platform.online_course.dto.request.ReviewRequest;
import com.kg.platform.online_course.dto.response.ProductDetailsResponse;
import com.kg.platform.online_course.dto.response.ReviewResponse;
import com.kg.platform.online_course.services.ProductService;
import com.kg.platform.online_course.services.ReviewService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@AllArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(name = "Review API", description = "The reviews API")
public class ReviewController {
    private final ReviewService reviewService;
    private final ProductService productService;

    @Operation(summary = "Post the new feedback",
            description = "This endpoint returns a new created feedback with all feedbacks")
    @PostMapping
    public ProductDetailsResponse addFeedback(@RequestBody ReviewRequest request, Principal principal){
        reviewService.createFeedback(request, principal);
        return productService.getProductById(request.getProductId());
    }

    @Operation(summary = "Delete the feedback", description = "This endpoint returns a deleted feedback")
    @PreAuthorize("hasAnyAuthority('ADMIN','SUPER_ADMIN')")
    @DeleteMapping("/{id}")
    public List<ReviewResponse> delete(@PathVariable Long id){
        return reviewService.delete(id);
    }
}
