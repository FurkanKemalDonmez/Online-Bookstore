package com.bookstore.api.controller;

import com.bookstore.api.request.LoginRequest;
import com.bookstore.api.request.SignupRequest;
import com.bookstore.api.response.AuthenticationResponse;
import com.bookstore.api.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Sign Up", description = "Register a new user.")
    @PostMapping("/signup")
    public ResponseEntity<AuthenticationResponse> signup(@RequestBody SignupRequest request) {
        return ResponseEntity.ok(userService.signUp(request));
    }

    @Operation(summary = "Login", description = "Authenticate a user and return a token.")
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(userService.login(request));
    }

}
