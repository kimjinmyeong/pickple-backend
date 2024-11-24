package com.pickple.user.application.service;

import com.pickple.common_module.exception.CustomException;
import com.pickple.user.application.dto.UserDto;
import com.pickple.user.application.dto.UserResponseDto;
import com.pickple.user.domain.model.User;
import com.pickple.user.domain.model.UserRole;
import com.pickple.user.domain.repository.UserRepository;
import com.pickple.user.exception.UserErrorCode;
import com.pickple.user.presentation.request.SignUpRequestDto;
import com.pickple.user.presentation.request.UpdateUserRequestDto;
import com.pickple.user.presentation.request.UserSearchDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    // 회원 전체 조회
    public List<UserResponseDto> getAllUsers() {
        return userRepository.findAllByIsDeleteFalse().stream()
                .map(UserResponseDto::from)
                .collect(Collectors.toList());
    }

    // 회원 상세 조회
    @Transactional(readOnly = true)
    public UserResponseDto getUsername(String username) {
        User user = findUserByUsername(username);
        return UserResponseDto.from(user);
    }

    // 회원 수정
    @Transactional
    public void updateUser(String username, UpdateUserRequestDto requestDto) {
        User user = findUserByUsername(username);
        user.updateUserInfo(requestDto.getNickname(), requestDto.getPassword());
    }

    // 회원 탈퇴 및 삭제
    @Transactional
    public void softDeleteUser(String username) {
        User user = findUserByUsername(username);
        user.markAsDeleted();
    }

    // 유저 권한 부여
    @Transactional
    public void updateUserRole(String username, UserRole role) {
        User user = findUserByUsername(username);
        //UserRole newRole = UserRole.fromString(role); // 문자열을 UserRole로 변환

        // 동일한 권한으로 변경 할 경우 예외 처리
        if (user.getRole().equals(role)) {
            throw new CustomException(UserErrorCode.ALREADY_SAME_ROLE);
        }

        user.updateRole(role);
    }

    // 유저 검색
    @Transactional(readOnly = true)
    public Page<UserResponseDto> searchUsers(UserSearchDto searchDto, Pageable pageable) {
        return userRepository.searchUsers(searchDto, pageable)
                .map(UserResponseDto::from);
    }

    // 유저 존재 여부 확인 메서드
    @Transactional(readOnly = true)
    public User findUserByUsername(String username) {
        return userRepository.findByUsernameAndIsDeleteFalse(username)
                .orElseThrow(() -> new CustomException(UserErrorCode.USER_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public UserDto getUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(UserErrorCode.USER_NOT_FOUND));
        return UserDto.convertToUserDto(user);
    }

    @Transactional
    public UserResponseDto registerUser(SignUpRequestDto signUpDto) {
        User user = User.convertSignUpDtoToUser(signUpDto);
        if (userRepository.existsByUsername(signUpDto.getUsername())) {
            log.error("중복된 username: {}", signUpDto.getUsername());
            throw new CustomException(UserErrorCode.USERNAME_ALREADY_EXISTS);
        }
        if (userRepository.existsByNickname(signUpDto.getNickname())) {
            log.error("중복된 nickname: {}", signUpDto.getNickname());
            throw new CustomException(UserErrorCode.NICKNAME_ALREADY_EXISTS);
        }
        if (userRepository.existsByEmail(signUpDto.getEmail())) {
            log.error("중복된 email: {}", signUpDto.getEmail());
            throw new CustomException(UserErrorCode.EMAIL_ALREADY_EXISTS);
        }
        User savedUser = userRepository.save(user);
        return UserResponseDto.from(savedUser);
    }

    @Transactional(readOnly = true)
    public String getUserEmailByUsername(String username) {
        return userRepository.findEmailByUsername(username)
                .orElseThrow(() -> new CustomException(UserErrorCode.USER_NOT_FOUND));
    }

}
