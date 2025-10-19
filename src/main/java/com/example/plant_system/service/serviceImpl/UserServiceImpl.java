package com.example.plant_system.service.serviceImpl;

import com.example.plant_system.dto.UserDto;
import com.example.plant_system.dto.UserTransformer;
import com.example.plant_system.exception.NullEntityReferenceException;
import com.example.plant_system.model.User;
import com.example.plant_system.repository.UserRepository;
import com.example.plant_system.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    @Override
    public UserDto create(UserDto userDto) {
        User user = UserTransformer.convertToEntity(userDto);
        user.setPassword(encoder.encode(user.getPassword()));
        user.setRoles("USER");
        user = userRepository.save(user);
        logger.debug("User created with ID: {}", user.getId());
        return UserTransformer.convertToDto(user);
    }

    @Override
    public User readById(Long id) {
        logger.info("Reading user by ID: {}", id);
        return userRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("User with id " + id + " not found"));
    }

    @Override
    public UserDto update(UserDto userDto) {
        User user = UserTransformer.convertToEntity(userDto);
        if ( readById(user.getId()) != null) {
            user.setPassword(encoder.encode(user.getPassword()));
            User newUser = userRepository.save(user);
            logger.debug("User updated with ID: {}", newUser.getId());
            return UserTransformer.convertToDto(newUser);
        }
        throw new NullEntityReferenceException("User cannot be 'null'");
    }


    @Override
    public void delete(Long id) {
        logger.info("Deleting user with ID: {}", id);
        userRepository.deleteById(id);
    }

    @Override
    public List<UserDto> getAll() {
        return userRepository.findAll().stream().map(UserTransformer::convertToDto).collect(Collectors.toList());
    }

    @Override
    public Long getUserIdByEmail(String email) {
        logger.info("Getting user ID by email: {}", email);
        return userRepository.findUserByEmail(email)
                .map(User::getId)
                .orElseThrow(() -> new EntityNotFoundException("User with email " + email + " not found"));
    }

}
