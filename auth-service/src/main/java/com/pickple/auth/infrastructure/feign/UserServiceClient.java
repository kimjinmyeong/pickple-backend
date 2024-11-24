package com.pickple.auth.infrastructure.feign;

import com.pickple.auth.application.dto.UserDto;
import com.pickple.auth.presentation.request.SignUpRequestDto;
import com.pickple.auth.presentation.response.UserResponseDto;
import com.pickple.common_module.presentation.dto.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "user-service")
public interface UserServiceClient {
	@GetMapping("/api/v1/users/user/{username}")
	UserDto getUserByUsername(@PathVariable("username") String username);

	@PostMapping("/api/v1/users/sign-up")
	ApiResponse<UserResponseDto> registerUser(@RequestBody SignUpRequestDto signUpDto);

}