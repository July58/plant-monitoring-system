package com.example.plant_system.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class NoteDto {

    private Long id;

    @NotBlank
    private String title;

    @NotEmpty
    private String text;


    private LocalDateTime noteDate;


    private Long userId;


    private Long plantId;

}
