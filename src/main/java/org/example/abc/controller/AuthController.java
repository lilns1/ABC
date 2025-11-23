package org.example.abc.controller;

import org.example.abc.dto.*;
import org.example.abc.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "认证管理", description = "用户注册与登录")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Operation(summary = "用户注册")
    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(@Valid @RequestBody RegisterRequest request) {
        if (authService.register(request)) {
            return ResponseEntity.status(201)
                    .body(new ApiResponse(true, "注册成功"));
        } else {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(false, "邮箱已被注册"));
        }
    }

    @Operation(summary = "用户登录")
    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@Valid @RequestBody LoginRequest request) {
        if (authService.authenticate(request)) {
            return ResponseEntity.ok()
                    .body(new ApiResponse(true, "登录成功"));
        } else {
            return ResponseEntity.status(401)
                    .body(new ApiResponse(false, "邮箱或密码错误"));
        }
    }
}