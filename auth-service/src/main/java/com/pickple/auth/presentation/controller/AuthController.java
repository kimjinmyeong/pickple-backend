package com.pickple.auth.presentation.controller;

import com.pickple.auth.application.dto.UserDto;
import com.pickple.auth.application.service.AuthService;
import com.pickple.auth.presentation.request.LoginRequestDto;
import com.pickple.auth.presentation.request.SignUpRequestDto;
import com.pickple.auth.presentation.response.UserResponseDto;
import com.pickple.common_module.presentation.dto.ApiResponse;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/sign-in")
    public ApiResponse<UserResponseDto> login(@Valid @RequestBody LoginRequestDto loginRequestDto, HttpServletResponse response) {
        UserResponseDto userDto = authService.login(loginRequestDto, response);
        return ApiResponse.success(HttpStatus.OK, "login success", userDto);
    }

    @PostMapping("/sign-up")
    public ApiResponse<UserResponseDto> signup(@RequestBody @Valid SignUpRequestDto signUpDto) {
        return authService.signup(signUpDto);
    }

}