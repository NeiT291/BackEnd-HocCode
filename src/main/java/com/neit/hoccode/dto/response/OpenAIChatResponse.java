package com.neit.hoccode.dto.response;

import java.util.List;

public record OpenAIChatResponse(
        List<Choice> choices
) {
    public record Choice(Message message) {}
    public record Message(String role, String content) {}
}