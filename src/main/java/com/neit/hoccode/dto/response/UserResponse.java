package com.neit.hoccode.dto.response;


import com.neit.hoccode.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private String displayName;
    private String username;
    private LocalDate dob;
    private String bio;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String email;
    private String phone;
    private String address;
    private Role role;
    private String avatarUrl;
    private Boolean isActive;
}
