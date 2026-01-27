package com.neit.hoccode.dto.request;

import java.util.List;

public record ChatRequest(
        List<Message> messages
) {
    public record Message(
            String role,   // system | user | assistant
            String content
    ) {}
}
