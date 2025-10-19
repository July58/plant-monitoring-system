package com.example.plant_system.controller;

import com.example.plant_system.config.security.jwt.JwtProvider;
import com.example.plant_system.dto.UserDto;
import com.example.plant_system.payload.request.UserRequest;
import com.example.plant_system.payload.response.MessageResponse;
import com.example.plant_system.payload.response.TokenResponse;
import com.example.plant_system.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth/")
public class AuthController {

    @Autowired
    private JwtProvider jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @PostMapping("/sign-in")
    public ResponseEntity<TokenResponse> authenticateAndGetToken(@RequestBody UserRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword())
        );
        if (authentication.isAuthenticated()) {
            TokenResponse tokenResponse = new TokenResponse();
            tokenResponse.setToken(jwtService.generateToken(authRequest.getEmail()));
            return ResponseEntity.ok(tokenResponse);
        } else {
            throw new UsernameNotFoundException("Invalid user request!");
        }
    }

    @PostMapping("/sign-up")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> register(@Valid @RequestBody UserDto userDto) {
        UserDto newUser = userService.create(userDto);
        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

}
