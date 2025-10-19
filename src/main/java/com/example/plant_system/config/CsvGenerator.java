package com.example.plant_system.config;

import com.example.plant_system.model.SensorData;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CsvGenerator {
    private static final String CSV_HEADER = "№,Temperature,Humidity,DS18B20Temp,SoilMoisture,Light,Date\n";

    public String createCsv(List<SensorData> sensorDataList) {
        StringBuilder csvContent = new StringBuilder();
        csvContent.append(CSV_HEADER);

        for (SensorData sensorData : sensorDataList) {
            csvContent.append(sensorData.getId()).append(",")
                    .append(sensorData.getTemperature()).append(",")
                    .append(sensorData.getHumidity()).append(",")
                    .append(sensorData.getDs18b20Temperature()).append(",")
                    .append(sensorData.getSoilMoisture()).append(",")
                    .append(sensorData.getPhotoresistorValue()).append(",")
                    .append(sensorData.getDataTime()).append("\n");
        }

        return csvContent.toString();
    }
}