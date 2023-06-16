package com.kg.platform.online_course.api_controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.kg.platform.online_course.dto.request.CourseCreateRequest;
import com.kg.platform.online_course.dto.request.ProductUpdateRequest;
import com.kg.platform.online_course.dto.response.CourseDetailsResponse;
import com.kg.platform.online_course.dto.response.ProductResponse;
import com.kg.platform.online_course.dto.response.SimpleResponse;
import com.kg.platform.online_course.services.CourseService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

import static org.springframework.data.domain.Sort.Direction.DESC;


@RestController
@RequestMapping("/api/products")
@AllArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(name = "Product API", description = "The products API for all ")
public class CourseController {
    private final CourseService courseService;
    private VideoController videoController;


    @Operation(summary = "Get main page",
            description = "This endpoint returns a list of popular and most sold products")
    @GetMapping
    public Map<String, List<ProductResponse>> getMainPage() {
        return courseService.getMainPage();
    }

    @Operation(summary = "Post the new product",
            description = "This endpoint returns a new created product with all products")
    @PreAuthorize("hasAnyAuthority('ADMIN','SUPER_ADMIN')")
    @PostMapping(path = "/add")
    public SimpleResponse addProduct(@RequestBody CourseCreateRequest request) {
        return courseService.create(request);
    }

    @Operation(summary = "Add image to product",
            description = "This endpoint returns a new created product with all products")
    @PreAuthorize("hasAnyAuthority('ADMIN','SUPER_ADMIN')")
    @PostMapping(path = "/{id}/add-image", consumes = {"multipart/form-data"})
    public ResponseEntity<CourseDetailsResponse> addVideo(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        return new ResponseEntity<>(courseService.addVideos(id, file), HttpStatus.OK);
    }

    @Operation(summary = "Delete image from product", description = "This endpoint delete image from product")
    @PreAuthorize("hasAnyAuthority('ADMIN','SUPER_ADMIN')")
    @DeleteMapping(path = "/{id}/delete")
    public ResponseEntity<CourseDetailsResponse> deleteImage(@PathVariable(name = "id") Long productId, Long imageId) {
        return new ResponseEntity<>(courseService.deleteImage(productId, imageId), HttpStatus.OK);
    }


    @Operation(summary = "Get all products",
            description = "This endpoint returns all products")
    @GetMapping("/get-all/{categoryId}")
    public Page<ProductResponse> getProductsByCategoryId(
            @PathVariable Long categoryId,
            @PageableDefault(sort = {"id"}, direction = DESC) Pageable pageable
    ) {
        return courseService.getAllProductsByCategoryId(categoryId, pageable);
    }

    @Operation(summary = "Get a product by id",
            description = "This endpoint returns product by product id")
    @GetMapping("/{id}")
    public CourseDetailsResponse getProductById(@PathVariable Long id) {
        return courseService.getProductById(id);
    }


    @Operation(summary = "Update the new product",
            description = "This endpoint returns a updated product with all products")
    @PreAuthorize("hasAnyAuthority('ADMIN','SUPER_ADMIN')")
    @PatchMapping
    public CourseDetailsResponse updateProductById(@RequestBody ProductUpdateRequest request) {
        return courseService.updateById(request);
    }

    @Operation(summary = "Delete the product",
            description = "This endpoint returns a deleted product with all products")
    @PreAuthorize("hasAnyAuthority('ADMIN','SUPER_ADMIN')")
    @DeleteMapping("/{id}")
    public Page<ProductResponse> deleteProductById(@PathVariable Long id) {
        return courseService.deleteById(id);
    }

    @Operation(summary = "Search by product name or description",
            description = "This endpoint returns a searched products by name and desc in  specified category if category id is present")
    @GetMapping("/search")
    public Page<ProductResponse> searchProduct(
            @RequestParam String name,
            @RequestParam(required = false) Long categoryId,
            @PageableDefault(sort = {"id"}, direction = DESC) Pageable pageable
    ) {
        return courseService.searchProduct(name, categoryId, pageable);
    }
}
