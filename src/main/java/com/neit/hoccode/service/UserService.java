package com.neit.hoccode.service;

import com.neit.hoccode.dto.request.ChangePasswordRequest;
import com.neit.hoccode.dto.request.RegisterRequest;
import com.neit.hoccode.dto.request.UpdateUserRequest;
import com.neit.hoccode.dto.response.UserResponse;
import com.neit.hoccode.entity.Contest;
import com.neit.hoccode.entity.Problem;
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
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final MinioService minioService;

    public UserService(UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder, MinioService minioService) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.minioService = minioService;
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

    public Void setAvatar(MultipartFile avatar) {
        User user = userRepository.findByUsername(SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getName())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        try{
            String objectName = minioService.uploadImage(avatar);
            String url = "http://localhost:9000/hoccode/" + objectName;
            user.setAvatarUrl(url);
            userRepository.save(user);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return null;
    }
    public void deActiveUser(String username){
        User user = userRepository.findByUsername(SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getName())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        if(Objects.equals(user.getRole().getName(), "ADMIN")){
            User user1 = userRepository.findByUsername(username).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
            user1.setIsActive(false);
            userRepository.save(user1);
        }
    }
    public UserResponse changePassword(ChangePasswordRequest request){
        User user = userRepository.findByUsername(SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getName())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        boolean isMatch = passwordEncoder.matches(request.getOldPassword(), user.getPassword());
        if (!isMatch){
            throw new AppException(ErrorCode.PASSWORD_NOT_MATCH);
        }
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.setUpdatedAt(LocalDateTime.now());
        return userMapper.toUserResponse(userRepository.save(user));
    }
}
