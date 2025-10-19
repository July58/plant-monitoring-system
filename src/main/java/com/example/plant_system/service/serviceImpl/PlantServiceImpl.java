package com.example.plant_system.service.serviceImpl;

import com.example.plant_system.dto.PhotoDto;
import com.example.plant_system.dto.PlantDto;
import com.example.plant_system.dto.PlantTransformer;
import com.example.plant_system.exception.NullEntityReferenceException;
import com.example.plant_system.model.Photo;
import com.example.plant_system.model.Plant;
import com.example.plant_system.model.User;
import com.example.plant_system.repository.PlantRepository;
import com.example.plant_system.service.PlantService;
import com.example.plant_system.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PlantServiceImpl implements PlantService {

    private final PlantRepository plantRepository;
    @Autowired
    private UserService userService;

    @Value("${plant.camera.stream.protocol:http}")
    private String cameraProtocol;

    @Value("${plant.camera.stream.port:81}")
    private String cameraPort;

    @Value("${plant.camera.stream.path:/stream}")
    private String cameraPath;

    private static final Logger logger = LoggerFactory.getLogger(PlantServiceImpl.class);

    public PlantServiceImpl(PlantRepository plantRepository) {
        this.plantRepository = plantRepository;
    }

    @Override
    public PlantDto create(PlantDto plantDto) {
        Long ownerId = plantDto.getOwnerId();
        User owner = userService.readById(ownerId);
        if (owner == null) {
            throw new EntityNotFoundException("User with id " + ownerId + " not found");
        }
        Plant plant = PlantTransformer.convertToEntity(plantDto);
        plant.setOwner(owner);
        plant.setPlantDate(LocalDateTime.now());
        plantRepository.save(plant);
        logger.debug("Plant created with id: {}", plant.getId());
        return PlantTransformer.convertToDto(plant);
    }

    @Override
    public Plant readById(Long id) {
        return plantRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Plant with id " + id + " not found"));
    }

    @Override
    public PlantDto getPlantInfo(Long id) {
        Plant plant = readById(id);
        if (plant.getCamera_url() != null) {
            plant.setCamera_url(
                    String.format("%s://%s:%s%s", cameraProtocol, plant.getCamera_url(), cameraPort, cameraPath)
            );
            logger.debug("Camera URL set for plant id {}: {}", id, plant.getCamera_url());
        }
        return PlantTransformer.convertToDto(plant);
    }

    @Override
    public PlantDto update(PlantDto plant) {
        if (plant != null) {
            Plant plantUpdate = readById(plant.getId());
            plantUpdate.setName(plant.getPlantName());
            plantUpdate.setType(plant.getType());
            plantUpdate.setInfo(plant.getInfo());
            plantUpdate.setLocation(plant.getLocation());
            plantUpdate.setCamera_url(plant.getCamera_ip());
            plantUpdate.setSystem_url(plant.getSystem_ip());
            plantRepository.save(plantUpdate);
            logger.debug("Plant updated with id: {}", plantUpdate.getId());
            return PlantTransformer.convertToDto(plantUpdate);
        }
        throw new NullEntityReferenceException("Plant cannot be 'null'");
    }

    @Override
    public void delete(Long id) {
        plantRepository.deleteById(id);
    }

    @Override
    public List<Plant> getAll() {
        return plantRepository.findAll().stream().collect(Collectors.toList());
    }

    @Override
    public List<PlantDto> getByUserId(Long userId) {
        return plantRepository.getPlantByUserId(userId).stream()
                .map(PlantTransformer::convertToDto).sorted(Comparator.comparing(PlantDto::getPlantName)).collect(Collectors.toList());
    }

    @Override
    public String getPlantAgeFormatted(Long plantId) {
        Plant plant = readById(plantId);
        LocalDateTime plantDate = plant.getPlantDate();
        LocalDateTime now = LocalDateTime.now();
        Period period = Period.between(plantDate.toLocalDate(), now.toLocalDate());
        int months = period.getMonths();
        int days = period.getDays();
        int years = period.getYears();
        if (years > 0) {
            return String.format("%d yr, %d mo, %d d", years, months, days);
        } else if (months > 0) {
            return String.format("%d mo, %d d", months, days);
        } else if (days > 0) {
            return String.format("%d d", days);
        } else if (ChronoUnit.HOURS.between(plantDate, now) > 0) {
            return String.format("%d hr", ChronoUnit.HOURS.between(plantDate, now));
        } else if (ChronoUnit.MINUTES.between(plantDate, now) >= 0) {
            return String.format("%d min", ChronoUnit.MINUTES.between(plantDate, now));
        }
        return "Invalid date provided!";}

    @Override
    public List<PhotoDto> getPlantPhotos(Long plantId) throws IOException {
        Plant plant = readById(plantId);
        List<Photo> photoList = plant.getPhotos();
        List<PhotoDto> photoDtoList = new ArrayList<>();

        for (Photo photo : photoList) {
            Path imagePath = Path.of(photo.getPath());
            if (!Files.exists(imagePath)) {
                logger.warn("Photo {} does not exist", photo.getPath());
                continue;
            }
            byte[] imageData = Files.readAllBytes(imagePath);
            PhotoDto photoDto = new PhotoDto();
            photoDto.setId(photo.getId());
            photoDto.setName(photo.getName());
            photoDto.setDateTime(photo.getDataTime());
            photoDto.setImageData(imageData);
            photoDtoList.add(photoDto);
        }

        Collections.sort(photoDtoList, Comparator.comparing(PhotoDto::getDateTime).reversed());
        return photoDtoList;
    }

}
