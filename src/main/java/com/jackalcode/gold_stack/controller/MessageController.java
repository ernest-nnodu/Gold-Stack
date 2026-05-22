package com.jackalcode.gold_stack.controller;

import com.jackalcode.gold_stack.dto.MessageResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

@RestController
public class MessageController {

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
