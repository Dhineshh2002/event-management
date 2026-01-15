package com.example.eventmanager.modules.user.controller;

import com.example.eventmanager.modules.user.dto.request.CreateUserRequest;
import com.example.eventmanager.modules.user.dto.request.InitiateUserRequest;
import com.example.eventmanager.modules.user.dto.response.UserResponse;
import com.example.eventmanager.modules.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

import static com.example.eventmanager.util.Utils.buildUri;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    @PostMapping("/initiate")
    public ResponseEntity<Void> initiateUser(@RequestBody @Valid InitiateUserRequest initiateUserRequest) {
        userService.initiateUser(initiateUserRequest);
        return ResponseEntity.ok().build();
    }

    @PostMapping
    public ResponseEntity<Void> createUser(@RequestBody @Valid CreateUserRequest createUserRequest) {
        Long userId = userService.createUser(createUserRequest);
        URI location = buildUri(userId, "/{id}");
        return ResponseEntity.created(location).build();
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getCurrentUser(@AuthenticationPrincipal Long userId) {
        UserResponse userResponse = userService.getUserById(userId);
        return ResponseEntity.ok(userResponse);
    }

}
