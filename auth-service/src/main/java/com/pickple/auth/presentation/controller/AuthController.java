package com.pickple.auth.presentation.controller;

import com.pickple.auth.application.service.AuthService;
import com.pickple.auth.presentation.request.LoginRequestDto;
import com.pickple.auth.presentation.request.SignUpRequestDto;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    /**
     * 로그인
     */
    @PostMapping("/sign-in")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto loginRequestDto, HttpServletResponse response) {
        try {
            authService.login(loginRequestDto, response);
            return ResponseEntity.ok().body("login success");
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login failed");
        }
    }

    /**
     *
     * 회원 가입
     */
    @PostMapping("/sign-up")
    public ResponseEntity<?> signup(@RequestBody @Valid SignUpRequestDto signUpDto) {
        Boolean result = authService.signup(signUpDto);
        if (result) {
            return ResponseEntity.ok().body("signup success");
        } else {
            return ResponseEntity.badRequest().body("signup failed");
        }
    }

}