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
public class Video {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String path;
    private Long videDuration;
    // Constructors, getters, and setters
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn()
    private Lesson lesson;


}
