package com.example.plant_system.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PhotoDto {

    private Long id;

    private String name;


    private byte [] imageData;

    private LocalDateTime dateTime;

    public PhotoDto(Long id, String name, LocalDateTime dateTime) {
        this.id = id;
        this.name = name;
        this.dateTime = dateTime;
    }
}
