package com.kg.platform.online_course.services;

import com.kg.platform.online_course.dto.request.CourseCreateRequest;
import com.kg.platform.online_course.dto.request.CourseUpdateRequest;
import com.kg.platform.online_course.dto.response.CourseDetailsResponse;
import com.kg.platform.online_course.dto.response.ProductResponse;
import com.kg.platform.online_course.mappers.CourseMapper;
import com.kg.platform.online_course.models.Category;
import com.kg.platform.online_course.models.Course;
import com.kg.platform.online_course.models.Image;
import com.kg.platform.online_course.repositories.CategoryRepository;
import com.kg.platform.online_course.repositories.CourseRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.webjars.NotFoundException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
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


    private final String UPLOAD_DIR="./uploads";
    public CourseDetailsResponse create(CourseCreateRequest request) {
        Course course = new Course(request);
        Category category = categoryRepository.findById(request.getCategoryId()).orElseThrow(() -> new NotFoundException("The category not found"));

        course.setCategory(category);
        courseRepository.save(course);

        String uploadPath = UPLOAD_DIR + "/" +request.getCourseName();
        File dir = new File(uploadPath);

        if (!dir.exists())
            dir.mkdir();

        return courseMapper.toCourseDetailsResponse(course);
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

        deleteCourseDirectory(Path.of("./uploads/" + course.getCourseName()));
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

    public  void deleteCourseDirectory(Path directory){
        try {

            // Delete all files within the directory
            Files.walk(directory)
                    .filter(Files::isRegularFile)
                    .forEach(file -> {
                        try {
                            Files.delete(file);
                        } catch (IOException e) {
                            System.out.println("Error deleting file: " + file.toString());
                        }
                    });

            // Delete the empty directory
            Files.delete(directory);

            System.out.println("Directory deleted successfully.");
        } catch (IOException e) {
            System.out.println("Error deleting the directory: " + e.getMessage());
        }
    }
}
