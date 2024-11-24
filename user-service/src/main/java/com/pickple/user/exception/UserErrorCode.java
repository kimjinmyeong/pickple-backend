package com.pickple.user.exception;

import com.pickple.common_module.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum UserErrorCode implements ErrorCode {
    // ------- 4xx --------
    FORBIDDEN(HttpStatus.FORBIDDEN, "접근이 거부되었습니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 유저가 존재하지 않습니다."),
    ALREADY_SAME_ROLE(HttpStatus.BAD_REQUEST, "유저 권한이 이미 동일한 권한으로 설정되어 있습니다."),
    USERNAME_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 존재하는 username입니다."),
    NICKNAME_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 존재하는 nickname입니다."),
    EMAIL_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 존재하는 email입니다.");

    private final HttpStatus status;
    private final String message;

    @Override
    public String getMessage() {
        return message;
    }
    @Override
    public HttpStatus getStatus() {
        return status;
    }

}

