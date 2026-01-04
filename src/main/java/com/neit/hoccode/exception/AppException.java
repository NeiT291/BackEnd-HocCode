package com.neit.hoccode.exception;

import lombok.*;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AppException extends RuntimeException {
    private ErrorCode errorCode;
}
