package com.jackalcode.gold_stack.entity;

import lombok.Getter;

import java.util.Objects;

@Getter
public class MessageEntity {

    private String message;

    public MessageEntity(String message) {
        validateMessage(message);
        this.message = message;
    }

    public void setMessage(String message) {
        validateMessage(message);
        this.message = message;
    }

    private void validateMessage(String message) {
        Objects.requireNonNull(message, "Message cannot be null");
        if (message.isBlank()) {
            throw new IllegalArgumentException("Message cannot be blank");
        }
    }
}
