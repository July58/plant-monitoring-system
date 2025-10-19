package com.example.plant_system.dto;

import com.example.plant_system.model.User;

public class UserTransformer {

    public static User convertToEntity(UserDto userDto) {
        User user = new User();
        user.setId(userDto.getId());
        user.setName(userDto.getFirstname());
        user.setSurname(userDto.getLastname());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
        user.setInfo(userDto.getInfo());
        return user;
    }

    public static UserDto convertToDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setFirstname(user.getName());
        userDto.setLastname(user.getSurname());
        userDto.setEmail(user.getEmail());
        userDto.setPassword(user.getPassword());
        userDto.setInfo(user.getInfo());
        return userDto;
    }




}
