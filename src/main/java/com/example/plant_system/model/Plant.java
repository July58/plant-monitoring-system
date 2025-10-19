package com.example.plant_system.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Data
@Entity
@Table(name = "plants")
public class Plant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    @NotBlank
    private String name;

    @Column(name = "type")
    @NotBlank
    private String type;

    @Column(name = "info")
    private String info;

    @Column(name = "system_url")
    private String system_url;

    @Column(name = "camera_url")
    private String camera_url;

    @Column(name = "location")
    @NotBlank
    private String location;

    @Column(name = "plantdate", nullable = false)
    private LocalDateTime plantDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User owner;

    @OneToMany(mappedBy = "plant", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<SensorData> sensorData;

    @OneToMany(mappedBy = "plant", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Photo> photos;

    @OneToMany(mappedBy = "plant", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private List<Note> notes;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Plant plant = (Plant) o;
        return Objects.equals(id, plant.id) && Objects.equals(name, plant.name) && Objects.equals(type, plant.type) && Objects.equals(info, plant.info) && Objects.equals(system_url, plant.system_url) && Objects.equals(camera_url, plant.camera_url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, type, info, system_url, camera_url);
    }
}
