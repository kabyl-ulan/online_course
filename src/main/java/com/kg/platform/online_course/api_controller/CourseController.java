package com.kg.platform.online_course.api_controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.kg.platform.online_course.dto.request.CourseCreateRequest;
import com.kg.platform.online_course.dto.request.CourseUpdateRequest;
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
@RequestMapping("/api/course")
@AllArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(name = "Course API", description = "The Course API  ")
public class CourseController {
    private final CourseService courseService;


    @Operation(summary = "Get main page",
            description = "This endpoint returns a list of popular and most sold courses")
    @GetMapping
    public Map<String, List<ProductResponse>> getMainPage() {
        return courseService.getMainPage();
    }

    @Operation(summary = "Post the new product",
            description = "This endpoint returns a new created product with all courses")
    @PreAuthorize("hasAnyAuthority('ADMIN','SUPER_ADMIN')")
    @PostMapping(path = "/add")
    public CourseDetailsResponse addProduct(@RequestBody CourseCreateRequest request) {
        return courseService.create(request);
    }

    @Operation(summary = "Add image to course",
            description = "This endpoint returns course with added image")
    @PreAuthorize("hasAnyAuthority('ADMIN','SUPER_ADMIN')")
    @PostMapping(path = "/{id}/add-image", consumes = {"multipart/form-data"})
    public ResponseEntity<CourseDetailsResponse> addImage(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        return new ResponseEntity<>(courseService.addImage(id, file), HttpStatus.OK);
    }




    @Operation(summary = "Get all products",
            description = "This endpoint returns all course by category")
    @GetMapping("/get-all/{categoryId}")
    public Page<ProductResponse> getCourseByCategoryId(
            @PathVariable Long categoryId,
            @PageableDefault(sort = {"id"}, direction = DESC) Pageable pageable
    ) {
        return courseService.getAllProductsByCategoryId(categoryId, pageable);
    }

    @Operation(summary = "Get a course by id",
            description = "This endpoint returns course by id")
    @GetMapping("/{id}")
    public CourseDetailsResponse getProductById(@PathVariable Long id) {
        return courseService.getCourseById(id);
    }


    @Operation(summary = "Update the course",
            description = "This endpoint returns a updated course ")
    @PreAuthorize("hasAnyAuthority('ADMIN','SUPER_ADMIN')")
    @PatchMapping
    public CourseDetailsResponse updateCourseById(@RequestBody CourseUpdateRequest request) {
        return courseService.updateById(request);
    }

    @Operation(summary = "Delete the course",
            description = "This endpoint returns a deleted course with all courses")
    @PreAuthorize("hasAnyAuthority('ADMIN','SUPER_ADMIN')")
    @DeleteMapping("/{id}")
    public Page<ProductResponse> deleteCourseById(@PathVariable Long id) {
        return courseService.deleteById(id);
    }

    @Operation(summary = "Search by course name or description",
            description = "This endpoint returns a searched courses by name and desc in  specified category " +
                    "if category id is present otherwise search globally in db")
    @GetMapping("/search")
    public Page<ProductResponse> searchCourse(
            @RequestParam String name,
            @RequestParam(required = false) Long categoryId,
            @PageableDefault(sort = {"id"}, direction = DESC) Pageable pageable
    ) {
        return courseService.searchCourse(name, categoryId, pageable);
    }
}
