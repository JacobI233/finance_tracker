package com.jacobIbrom.finance_tracker.controller;

import com.jacobIbrom.finance_tracker.dto.UserResponse;
import com.jacobIbrom.finance_tracker.model.User;
import com.jacobIbrom.finance_tracker.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<UserResponse> getCurrentUser(Authentication authentication){
        User user = userService.getCurrentUser(authentication.getName());
        return ResponseEntity.ok(new UserResponse(user.getId(), user.getName(), user.getEmail()));

    }
}
