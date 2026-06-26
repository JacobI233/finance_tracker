package com.jacobIbrom.finance_tracker.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jacobIbrom.finance_tracker.dto.LoginRequest;
import com.jacobIbrom.finance_tracker.dto.RegisterRequest;
import com.jacobIbrom.finance_tracker.model.User;
import com.jacobIbrom.finance_tracker.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request){
        User user = userService.register(request);
        
        return ResponseEntity.ok("User registered: " + user.getEmail());

    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request){
        String token = userService.login(request);

        return ResponseEntity.ok(token);

    }


}
