package com.example.plant_system.controller;


import com.example.plant_system.config.security.userDetails.CustomUserDetails;
import com.example.plant_system.dto.PlantDto;
import com.example.plant_system.dto.PlantTransformer;
import com.example.plant_system.dto.SensorDataDto;
import com.example.plant_system.model.Plant;
import com.example.plant_system.payload.response.MessageResponse;
import com.example.plant_system.payload.response.PlantResponse;
import com.example.plant_system.service.PlantService;
import com.example.plant_system.service.SensorDataService;
import com.example.plant_system.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/plants")
public class PlantController {

    @Autowired
    PlantService plantService;

    @Autowired
    UserService userService;

    @Autowired
    SensorDataService sensorDataService;

    @Value("${plant.camera.default.url}")
    private String defaultCameraUrl;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> createPlant(@Valid @RequestBody PlantDto plantDto) {
        plantDto.setOwnerId(getUserId());
        plantService.create(plantDto);

        return ResponseEntity.ok(new MessageResponse("Plant created successfully!"));
    }

    @DeleteMapping("/{plantId}")
    public List<PlantDto> deletePlantById(@PathVariable(name = "plantId") Long plantId) {
        plantService.delete(plantId);
        return getAllPlantsByUser();
    }

    @PutMapping
    public ResponseEntity<?> updatePlant(@Valid @RequestBody PlantDto plantDto) {
        plantService.update(plantDto);
        return ResponseEntity.ok(new MessageResponse("Plant updated successfully!"));
    }


    @GetMapping("/plant-list")
    public List<PlantDto> getAllPlantsByUser() {
        List<PlantDto> plantDtoList = plantService.getByUserId(getUserId());
        return plantDtoList;
    }

    @GetMapping("/plant-info/{plantId}")
    public ResponseEntity<?> getPlantInfo(@PathVariable(name = "plantId") Long plantId) {
        SensorDataDto sensorDataDtoCurrent = sensorDataService.getSensorDataForPlant(plantService.readById(plantId));
        PlantDto plantDto = plantService.getPlantInfo(plantId);
        PlantResponse plantResponse = null;
        if (!plantDto.getSensorDataList().isEmpty() && !plantDto.getCamera_ip().isBlank() && sensorDataDtoCurrent!=null) {
            plantResponse = new PlantResponse(plantDto.getId(),
                    plantDto.getPlantName(),
                    plantDto.getType(),
                    plantDto.getInfo(),
                    plantDto.getLocation(),
                    plantService.getPlantAgeFormatted(plantId),
                    plantDto.getSystem_ip(),
                    plantDto.getCamera_ip(),
                    plantDto.getOwnerId(),
                    sensorDataDtoCurrent);
        } else {
            SensorDataDto sensorDataDto = new SensorDataDto(0L, 0.0, 0.0, 0.0, 0.0, 0, LocalDateTime.now());
            plantDto.getSensorDataList().add(sensorDataDto);
            plantDto.setCamera_ip(defaultCameraUrl);
            plantResponse = new PlantResponse(
                    plantDto.getId(),
                    plantDto.getPlantName(),
                    plantDto.getType(),
                    plantDto.getInfo(),
                    plantDto.getLocation(),
                    plantService.getPlantAgeFormatted(plantId),
                    plantDto.getSystem_ip(),
                    plantDto.getCamera_ip(),
                    plantDto.getOwnerId(),
                    plantDto.getSensorDataList().get(0)
            );
        }
        return ResponseEntity.ok(plantResponse);
    }

    @GetMapping("/plant-general-info/{plantId}")
    public ResponseEntity<?> getPlantBaseInfo(@PathVariable(name = "plantId") Long plantId) {
        Plant plant = plantService.readById(plantId);
        PlantDto plantDto = PlantTransformer.convertToDto(plant);
        return ResponseEntity.ok(plantDto);
    }

    @GetMapping("/get-plant-name/{plantId}")
    public String getPlantName(@PathVariable("plantId") Long plantId){
        return plantService.readById(plantId).getName();
    }

    public Long getUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email =  ((CustomUserDetails) authentication.getPrincipal()).getUsername();
        return userService.getUserIdByEmail(email);
    }
}
