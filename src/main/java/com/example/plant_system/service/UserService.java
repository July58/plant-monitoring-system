package com.example.plant_system.service;

import com.example.plant_system.dto.UserDto;
import com.example.plant_system.model.User;

import java.util.List;

public interface UserService {
    UserDto create(UserDto user);
    User readById(Long id);
    UserDto update(UserDto user);
    void delete(Long id);
    List<UserDto> getAll();
    Long getUserIdByEmail(String email);
}
