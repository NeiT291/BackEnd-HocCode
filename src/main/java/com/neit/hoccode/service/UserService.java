package com.neit.hoccode.service;

import com.neit.hoccode.dto.request.RegisterRequest;
import com.neit.hoccode.dto.request.UpdateUserRequest;
import com.neit.hoccode.dto.response.UserResponse;
import com.neit.hoccode.entity.Role;
import com.neit.hoccode.entity.User;
import com.neit.hoccode.exception.AppException;
import com.neit.hoccode.exception.ErrorCode;
import com.neit.hoccode.mapper.UserMapper;
import com.neit.hoccode.repository.UserRepository;
import com.neit.hoccode.utils.MergeObject;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public UserResponse register(RegisterRequest request) {
        if (!Objects.equals(request.getPassword(), request.getRepassword())){
            throw new AppException(ErrorCode.PASSWORD_NOT_MATCH);
        }
        User user = userMapper.toUser(request);
        user.setRole(Role.builder().id(2).build());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setCreatedAt(LocalDateTime.now());
        return userMapper.toUserResponse(userRepository.save(user));
    }

    public void delete(String username){
        User user = userRepository.findByUsername(username).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        userRepository.deleteById(user.getId());
    }
    public UserResponse getMyInfo(){
        return userMapper.toUserResponse(userRepository.findByUsername(SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND)));
    }

    public UserResponse updateInfo(UpdateUserRequest request) {
        User user = userRepository.findByUsername(SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getName())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        User requestUser = userMapper.toUser(request);
        MergeObject.mergeIgnoreNull(requestUser, user);
        return userMapper.toUserResponse(userRepository.save(user));
    }
    public void getAllUser(){
    }
}
