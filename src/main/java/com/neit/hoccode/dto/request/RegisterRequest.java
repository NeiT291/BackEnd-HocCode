package com.neit.hoccode.dto.request;

import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class RegisterRequest {
    private String username;
    @Size(min = 8)
    private String password;
    private String repassword;
    private String display_name;
    private LocalDate dob;
    private String email;
    private String phone;
    private String address;
}
