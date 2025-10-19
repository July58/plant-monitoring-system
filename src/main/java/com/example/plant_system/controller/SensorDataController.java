package com.example.plant_system.controller;

import com.example.plant_system.config.CsvGenerator;
import com.example.plant_system.dto.AggregatedSensorDataDto;
import com.example.plant_system.model.SensorData;
import com.example.plant_system.service.PlantService;
import com.example.plant_system.service.SensorDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/api")
public class SensorDataController {

    @Autowired
    private SensorDataService sensorDataService;

    @Autowired
    private PlantService plantService;

    @Autowired
    private CsvGenerator csvGenerator;

    @GetMapping("/{plantId}/sensor-data")
    public List<AggregatedSensorDataDto> getSensorData(
            @PathVariable Long plantId,
            @RequestParam String startDate,
            @RequestParam String endDate,
            @RequestParam String interval) {

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        LocalDate startLocalDate = LocalDate.parse(startDate, dateFormatter);
        LocalDateTime start = startLocalDate.atStartOfDay();
        LocalDate endLocalDate = LocalDate.parse(endDate, dateFormatter);
        LocalDateTime end = endLocalDate.atTime(LocalTime.MAX);
        List<AggregatedSensorDataDto> sensorDataDtoList = sensorDataService.getAggregatedSensorData(plantService.readById(plantId), start, end, interval);
        return sensorDataDtoList;
    }

    @GetMapping("{plantId}/generate-csv")
    public ResponseEntity<byte[]> generateCsvFile(@PathVariable Long plantId) {
        List<SensorData> sensorDataList = sensorDataService.getListForPlant(plantService.readById(plantId));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "sensor-data.csv");

        byte[] csvBytes = csvGenerator.createCsv(sensorDataList).getBytes();

        return new ResponseEntity<>(csvBytes, headers, HttpStatus.OK);
    }
}
