package com.jackalcode.gold_stack.controller;

import com.jackalcode.gold_stack.entity.MessageEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MessageController {

    @GetMapping
    public ResponseEntity<MessageEntity> getHappyMessage() {

    MessageEntity message = new MessageEntity("Hello From Gold Stack in AWS!");
        return ResponseEntity.ok(message);
    }

    @GetMapping(path = "/error")
    public ResponseEntity<MessageEntity> getErrorMessage() {

        var message = new MessageEntity("This is an error message from Gold Stack in AWS!");
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(message);
    }

    @GetMapping(path = "/custom/**")
    public ResponseEntity<MessageEntity> getCustomMessage() {

        var message = new MessageEntity("Message not found in Gold Stack in AWS!");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
    }
}
