package com.pickple.auth.presentation.response;

import com.pickple.auth.application.domain.model.User;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;


@Getter
@Builder
public class UserResponseDto {

	private String username;
	private List<String> roles;

	public static UserResponseDto fromUser(User user) {
		return UserResponseDto.builder()
				.username(user.getUsername())
				.roles(new ArrayList<>(user.getRoles()))
				.build();
	}
}

