package com.jackalcode.gold_stack.controller;

import com.jackalcode.gold_stack.entity.MessageEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    @GetMapping
    public ResponseEntity<MessageEntity> getHappyMessage() {

    MessageEntity message = new MessageEntity("Hello From Gold Stack in AWS!");
        return ResponseEntity.ok(message);
    }
}
