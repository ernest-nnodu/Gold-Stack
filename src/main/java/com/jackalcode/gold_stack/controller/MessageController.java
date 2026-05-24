package com.jackalcode.gold_stack.controller;

import com.jackalcode.gold_stack.dto.CreateMessageRequest;
import com.jackalcode.gold_stack.dto.MessageResponse;
import com.jackalcode.gold_stack.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @GetMapping(path = "/messages")
    public ResponseEntity<List<MessageResponse>> getMessages() {

        return ResponseEntity.ok(messageService.getMessages());
    }

    @GetMapping(path = "/messages/{id}")
    public ResponseEntity<MessageResponse> getMessage(Long id) {

        return ResponseEntity.ok(messageService.getMessage(id));
    }

    @PostMapping(path = "/messages")
    public ResponseEntity<MessageResponse> createMessage(CreateMessageRequest messageRequest) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(messageService.createMessage(messageRequest));
    }

    @GetMapping
    public ResponseEntity<MessageResponse> getHappyMessage() {

    MessageResponse message = new MessageResponse(
            0L,
            "Happy Message",
            "Hello From Gold Stack in AWS!",
            Instant.now().toString());
        return ResponseEntity.ok(message);
    }

    @GetMapping(path = "/error")
    public ResponseEntity<MessageResponse> getErrorMessage() {

        var message = new MessageResponse(
                0L,
                "Error Message",
                "This is an error message from Gold Stack in AWS!",
                Instant.now().toString());
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(message);
    }

    @GetMapping(path = "/custom/**")
    public ResponseEntity<MessageResponse> getCustomMessage() {

        var message = new MessageResponse(
                0L,
                "Not Found",
                "Message not found in Gold Stack in AWS!",
                Instant.now().toString());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
    }
}
