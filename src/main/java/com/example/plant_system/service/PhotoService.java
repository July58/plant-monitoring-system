package com.example.plant_system.service;

import com.example.plant_system.dto.PhotoDto;
import com.example.plant_system.model.Photo;
import com.example.plant_system.model.Plant;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public interface PhotoService {

    PhotoDto capturePhoto(Plant plant) throws IOException;

    void deletePhoto(Long id) throws IOException;

    Photo readById(Long id);

    List<PhotoDto> getPhotoByPeriod(Plant plant, LocalDateTime start, LocalDateTime end);

    void createAndSaveGif(Plant plant, LocalDateTime startDate, LocalDateTime endDate);
}
