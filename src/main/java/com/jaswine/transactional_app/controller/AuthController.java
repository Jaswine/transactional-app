package com.jaswine.transactional_app.controller;

import com.jaswine.transactional_app.db.dto.LoginRequestDto;
import com.jaswine.transactional_app.db.dto.RegisterRequestDto;
import com.jaswine.transactional_app.db.dto.TokenResponseDto;
import com.jaswine.transactional_app.db.dto.UserResponseDto;
import com.jaswine.transactional_app.db.entity.User;
import com.jaswine.transactional_app.services.AuthService;
import com.jaswine.transactional_app.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequestDto request) {
        authService.register(request.getUsername(), request.getEmail(), request.getPassword());
        return ResponseEntity.ok("");
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponseDto> login(@RequestBody LoginRequestDto request) {
        Optional<User> optionalUser = authService.login(request.getEmail(), request.getPassword());
        return optionalUser.map(user -> ResponseEntity.ok(new TokenResponseDto(
                JwtUtils.generateAccessToken(user.getEmail()),
                JwtUtils.generateRefreshToken(user.getEmail()))
            )).orElseGet(() -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());

    }
}
