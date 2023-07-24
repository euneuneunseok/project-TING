package com.ssafy.tingbackend.user.service;

import com.ssafy.tingbackend.entity.user.User;
import com.ssafy.tingbackend.security.JwtAuthenticationProvider;
import com.ssafy.tingbackend.security.JwtUtil;
import com.ssafy.tingbackend.user.dto.UserDto;
import com.ssafy.tingbackend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
//    private final JwtAuthenticationProvider jwtAuthenticationProvider;

    public Map<String, String> login(UserDto userDto) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userDto.getEmail(), userDto.getPassword());

//        Authentication authenticate = jwtAuthenticationProvider.authenticate(authenticationToken);
//        User principal = (User) authenticate.getPrincipal();
//
//        String accessToken = JwtUtil.generateAccessToken(String.valueOf(principal.getId()));
//        String refreshToken = JwtUtil.generateRefreshToken(String.valueOf(principal.getId()));
//
//        Map<String, String> result = new HashMap<>();
//        result.put("access-token", accessToken);
//        result.put("refresh-token", refreshToken);

//        return result;
        return null;
    }

    public User getUserInfo(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 email 입니다."));
    }
}
