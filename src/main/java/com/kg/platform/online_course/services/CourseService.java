package com.kg.platform.online_course.services;

import com.kg.platform.online_course.api_controller.VideoController;
import com.kg.platform.online_course.dto.request.CourseCreateRequest;
import com.kg.platform.online_course.dto.request.CourseUpdateRequest;
import com.kg.platform.online_course.dto.response.CourseDetailsResponse;
import com.kg.platform.online_course.dto.response.ProductResponse;
import com.kg.platform.online_course.dto.response.SimpleResponse;
import com.kg.platform.online_course.mappers.CourseMapper;
import com.kg.platform.online_course.models.Category;
import com.kg.platform.online_course.models.Course;
import com.kg.platform.online_course.models.Image;
import com.kg.platform.online_course.repositories.*;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.webjars.NotFoundException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
@Transactional
public class CourseService {
    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;
    private final CategoryRepository categoryRepository;
    private final CartItemRepository cartItemRepository;
    private final OrderItemRepository orderItemRepository;
    private final ImageRepository imageRepository;

    private final VideoController videoController;

    public SimpleResponse create(CourseCreateRequest request) {
        Course course = new Course(request);
        Category category = categoryRepository.findById(request.getCategoryId()).orElseThrow(() -> new NotFoundException("The category not found"));
        course.setCategory(category);
        courseRepository.save(course);
        return new SimpleResponse("Successfully added", "SAVE");
    }


    public Page<ProductResponse> getAllProductsByCategoryId(Long categoryId, Pageable pageable) {

        return courseRepository.findAllByCategoryIdOrderByUploadDateDesc(categoryId, pageable).map(courseMapper::toProductResponse);
    }

    public CourseDetailsResponse getCourseById(Long id) {
        Course course = courseRepository.findById(id).orElseThrow(() -> new NotFoundException("Product was not found"));
        return courseMapper.toCourseDetailsResponse(course);
    }

    public CourseDetailsResponse updateById(CourseUpdateRequest request) {
        Course course = courseRepository.findById(request.getProductId()).orElseThrow(() -> new NotFoundException("Product with id=" + request.getProductId() + "not found"));
        course.setCourseName(request.getCourseName());
        course.setDescription(request.getDescription());
        course.setPrice(request.getPrice());
        return getCourseById(course.getId());
    }

    public Page<ProductResponse> deleteById(Long id) {
        Pageable pageable = Pageable.unpaged();
        Course course = courseRepository.findById(id).orElseThrow(() -> new NotFoundException("Not found"));


        courseRepository.delete(course);
        return getAllProductsByCategoryId(course.getCategory().getId(), pageable);
    }


    public CourseDetailsResponse addImage(Long id, MultipartFile file) {
        Course course = courseRepository.findById(id).orElseThrow(() -> new NotFoundException("Not found"));
        course.setCourseImage(Image.parseImage(file));
        courseRepository.save(course);
        return courseMapper.toCourseDetailsResponse(course);
    }




    public CourseDetailsResponse deleteImage(Long productId, Long imageId) {
        Course course = courseRepository.findById(productId).get();
        courseRepository.save(course);
        return courseMapper.toCourseDetailsResponse(course);
    }


    public List<ProductResponse> getTopSoldProducts() {
        return courseRepository
                .findTopBySoldOrderBySoldDesc()
                .stream()
                .map(courseMapper::toProductResponse)
                .toList();
    }

    public List<ProductResponse> getLatestProducts() {
        return courseRepository.findLatestProducts()
                .stream()
                .map(courseMapper::toProductResponse)
                .toList();
    }

    public Map<String, List<ProductResponse>> getMainPage() {

        Map<String, List<ProductResponse>> body = new HashMap<>();
        List<ProductResponse> topSold = getTopSoldProducts();
        List<ProductResponse> latest = getLatestProducts();

        body.put("topSold", topSold);
        body.put("latest", latest);
        return body;
    }

    public Page<ProductResponse> searchCourse(String name, Long categoryId, Pageable pageable) {
        if (categoryId == null) {
            return courseRepository.findByCourseNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(name, name, pageable).map(courseMapper::toProductResponse);
        } else {
            return courseRepository.findByCourseNameContainingIgnoreCaseOrDescriptionContainingIgnoreCaseAndCategoryId(name, name, categoryId, pageable).map(courseMapper::toProductResponse);
        }
    }
}
