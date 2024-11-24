package com.pickple.auth.presentation.response;


import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public class UserResponseDto {

	private String username;
	private String nickname;
	private String email;
	private String role;

}

