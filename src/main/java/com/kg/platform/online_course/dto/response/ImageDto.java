package com.kg.platform.online_course.dto.response;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImageDto {
    private Long id;
    private byte[] data;
    private String fileType;
}
