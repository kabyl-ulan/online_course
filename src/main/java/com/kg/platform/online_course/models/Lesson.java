package com.kg.platform.online_course.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Lesson {
    @Id
    private Long id;
    private String title;

    @OneToOne(cascade = CascadeType.ALL)
    private Video video;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn()
    private Course course;
}
