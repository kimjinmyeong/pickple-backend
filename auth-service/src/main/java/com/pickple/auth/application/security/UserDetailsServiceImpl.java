package com.pickple.auth.application.security;

import com.pickple.auth.application.domain.model.User;
import com.pickple.auth.application.dto.UserDto;
import com.pickple.auth.infrastructure.feign.UserServiceClient;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserServiceClient userServiceClient;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        try {
            UserDto userDto = userServiceClient.getUserByUsername(username);
            User user = User.convertToUser(userDto);
            return new UserDetailsImpl(user);
        } catch (FeignException e) {
            throw new UsernameNotFoundException("사용자가 존재하지 않습니다.", e);
        }
    }
}
