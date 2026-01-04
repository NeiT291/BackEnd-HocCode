package com.neit.hoccode.mapper;

import com.neit.hoccode.dto.request.RegisterRequest;
import com.neit.hoccode.dto.request.UpdateUserRequest;
import com.neit.hoccode.dto.response.UserResponse;
import com.neit.hoccode.entity.User;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(RegisterRequest request);
    User toUser(UpdateUserRequest request);
    UserResponse toUserResponse(User user);
}
