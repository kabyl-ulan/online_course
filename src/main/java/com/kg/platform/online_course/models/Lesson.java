package com.kg.platform.online_course.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Lesson {
    private static final String SEQ_NAME = "lesson_seq";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQ_NAME)
    @SequenceGenerator(name = SEQ_NAME, sequenceName = SEQ_NAME, allocationSize = 1, initialValue = 1)
    private Long id;
    private String title;
    @OneToOne( fetch = FetchType.EAGER,cascade = CascadeType.ALL,orphanRemoval = true)
    private Video video;

    private String path;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn()
    private Course course;
}
