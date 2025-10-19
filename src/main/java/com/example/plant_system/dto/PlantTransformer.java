package com.example.plant_system.dto;

import com.example.plant_system.model.Plant;

import java.util.Collections;
import java.util.stream.Collectors;

public class PlantTransformer {

    public static PlantDto convertToDto(Plant plant) {
        return new PlantDto(
                plant.getId(),
                plant.getName(),
                plant.getType(),
                plant.getInfo(),
                plant.getSystem_url(),
                plant.getCamera_url(),
                plant.getOwner() != null ? plant.getOwner().getId() : null,
                plant.getLocation(),
                plant.getPlantDate(),
                plant.getSensorData() != null
                        ? plant.getSensorData().stream()
                        .map(SensorDataTransformer::convertToDto)
                        .collect(Collectors.toList())
                        : Collections.emptyList(),
                plant.getPhotos() != null
                        ? plant.getPhotos().stream()
                        .map(PhotoTransformer::convertToDto)
                        .collect(Collectors.toList())
                        : Collections.emptyList()
        );
    }


    public static Plant convertToEntity(PlantDto plantDto){
        Plant plant = new Plant();
        plant.setName(plantDto.getPlantName());
        plant.setType(plantDto.getType());
        plant.setInfo(plantDto.getInfo());
        plant.setCamera_url(plantDto.getCamera_ip());
        plant.setSystem_url(plantDto.getSystem_ip());
        plant.setLocation(plantDto.getLocation());
        plant.setPlantDate(plantDto.getPlantDate());
        return plant;
    }
}
