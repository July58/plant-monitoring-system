package com.example.plant_system.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Objects;


@Data
@Entity
@Table(name = "notes")
public class Note {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    @NotBlank
    private String title;

    @Column(name = "text")
    @NotEmpty
    private String text;

    @Column(name = "datatime")
    private LocalDateTime dataTime;

    @ManyToOne
    @JoinColumn(name = "plant_id")
    private Plant plant;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Note note = (Note) o;
        return Objects.equals(id, note.id) && Objects.equals(title, note.title) && Objects.equals(text, note.text) && Objects.equals(dataTime, note.dataTime) && Objects.equals(plant, note.plant) && Objects.equals(user, note.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, text, dataTime, plant, user);
    }
}
