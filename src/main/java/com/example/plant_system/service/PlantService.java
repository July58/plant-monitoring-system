package com.example.plant_system.service;

import com.example.plant_system.dto.PhotoDto;
import com.example.plant_system.dto.PlantDto;
import com.example.plant_system.model.Plant;

import java.io.IOException;
import java.util.List;

public interface PlantService {
    PlantDto create(PlantDto plant);
    Plant readById(Long id);
    PlantDto getPlantInfo(Long id);
    PlantDto update(PlantDto plant);
    void delete(Long id);
    List<Plant> getAll();
    List<PlantDto> getByUserId(Long userId);
    String getPlantAgeFormatted(Long plantId);
    List<PhotoDto> getPlantPhotos(Long plantId) throws IOException;

}
