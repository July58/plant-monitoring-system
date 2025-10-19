package com.example.plant_system.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;
import java.util.Objects;

@Data
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email", nullable = false, unique = true)
    @NotBlank
    private String email;

    @Column(name = "password", nullable = false)
    @NotBlank
    private String password;

    @Column(name = "name", nullable = false)
    @NotBlank
    private String name;

    @Column(name = "surname", nullable = false)
    @NotBlank
    private String surname;

    @Column(name = "info")
    private String info;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.REMOVE)
    private List<Plant> myPlants;

    @Column(name = "roles")
    private String roles;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(email, user.email) && Objects.equals(password, user.password) && Objects.equals(name, user.name) && Objects.equals(surname, user.surname) && Objects.equals(info, user.info);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, password, name, surname, info);
    }
}
