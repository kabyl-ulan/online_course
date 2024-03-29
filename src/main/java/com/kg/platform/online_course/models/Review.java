package com.kg.platform.online_course.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "reviews")

public class Review {
    private static final String SEQ_NAME = "review_seq";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQ_NAME)
    @SequenceGenerator(name = SEQ_NAME, sequenceName = SEQ_NAME, allocationSize = 1, initialValue = 5)
    private Long id;
    private String text;
    private LocalDate date;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn()
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn()
    private Course course;
}
