package com.example.plant_system.dto;

import lombok.*;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;



@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PlantDto {

    private Long id;

    @NotBlank
    private String plantName;

    @NotBlank
    private String type;

    @NotBlank
    private String info;

    private String system_ip;

    private String camera_ip;

    private Long ownerId;

    private String location;

    private LocalDateTime plantDate;

    private List<SensorDataDto> sensorDataList;

    private List<PhotoDto> photoList;

}
