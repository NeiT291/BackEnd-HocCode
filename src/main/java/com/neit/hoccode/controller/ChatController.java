package com.neit.hoccode.controller;

import com.neit.hoccode.dto.request.ChatRequest;
import com.neit.hoccode.dto.response.ChatResponse;
import com.neit.hoccode.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @PostMapping
    public ChatResponse chat(@RequestBody ChatRequest request) {
        String reply = chatService.chat(request);
        return new ChatResponse(reply);
    }
}
