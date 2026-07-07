package com.jackalcode.gold_stack.controller;

import com.jackalcode.gold_stack.dto.QuestionRequest;
import com.jackalcode.gold_stack.dto.RagAnswer;
import com.jackalcode.gold_stack.service.impl.MessageRagService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MessageRagController {

    private final MessageRagService messageRagService;

    @PostMapping(path = "messages/ask")
    public ResponseEntity<RagAnswer> askQuestion(
            @Valid @RequestBody QuestionRequest request) {

        var response = messageRagService.ask(request.question());
        return ResponseEntity.ok(response);
    }
}
