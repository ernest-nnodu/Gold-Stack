package com.jackalcode.gold_stack.exception;

public class MessageNotFoundException extends RuntimeException {

    public MessageNotFoundException(Long id) {

        super("Message with id: " + id + " not found");
    }
}
