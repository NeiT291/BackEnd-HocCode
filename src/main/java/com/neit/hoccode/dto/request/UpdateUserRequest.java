package com.neit.hoccode.dto.request;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UpdateUserRequest {
    private String password;
    private String displayName;
    private String bio;
    private LocalDate dob;
    private String email;
    private String phone;
    private String address;
}
