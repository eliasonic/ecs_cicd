package com.example.ecs_cicd.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "photos")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Photo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String objectKey;

    private String description;

    @Column(columnDefinition = "TEXT")
    private String presignedUrl;
}
