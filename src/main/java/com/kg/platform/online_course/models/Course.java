package com.kg.platform.online_course.models;

import com.kg.platform.online_course.dto.request.CourseCreateRequest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "course")

public class Course {
    private static final String SEQ_NAME = "course_seq";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQ_NAME)
    @SequenceGenerator(name = SEQ_NAME, sequenceName = SEQ_NAME, allocationSize = 1, initialValue = 10)
    private long id;
    private double price;
    private int lessonsNumber;
    @Column(name = "sold",columnDefinition = "int default 0")
    private int sold;

    private String courseName;
    private String description;
    private String author;

    @Column(name = "uploadDate", columnDefinition = "DATE DEFAULT CURRENT_DATE")
    private LocalDate uploadDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Category category;
    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Review> reviews = new HashSet<>();

    @OneToOne()
    @JoinColumn
    private Image courseImage;

    @OneToMany(mappedBy = "course",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Lesson> lessons = new ArrayList<>();


    public Course(CourseCreateRequest request) {
        this.courseName = request.getProductName();
        this.description = request.getDescription();
        this.price = request.getPrice();
        this.uploadDate = LocalDate.now();
        this.author = request.getAuthor();

    }
}


