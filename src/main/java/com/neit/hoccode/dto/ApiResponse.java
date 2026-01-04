package com.neit.hoccode.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiResponse<T>{
    @Builder.Default
    private int code = 200;
    @Builder.Default
    private String message = "success";
    private T data;
}
