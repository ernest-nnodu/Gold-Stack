package com.jackalcode.gold_stack.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.*;

public class MessageControllerUnitTest {

    private MessageController messageController;

    @BeforeEach
    public void setUp() {
        messageController = new MessageController();
    }

    @Test
    @DisplayName("getHappyMessage should return happy message and status 200")
    public void getHappyMessage_returnsHappyMessageAndStatus200() {

        var response = messageController.getHappyMessage();

        assertAll(
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertNotNull(response.getBody()),
                () -> assertEquals("Hello From Gold Stack in AWS!", response.getBody().getMessage())
        );
    }

    @Test
    @DisplayName("getErrorMessage should return error message and status 502")
    public void getErrorMessage_returnsErrorMessageAndStatus502() {

        var response = messageController.getErrorMessage();

        assertAll(
                () -> assertEquals(HttpStatus.BAD_GATEWAY, response.getStatusCode()),
                () -> assertNotNull(response.getBody()),
                () -> assertEquals("This is an error message from Gold Stack in AWS!", response.getBody().getMessage())
        );
    }

    @Test
    @DisplayName("getCustomMessage should return custom message and status 404")
    public void getCustomMessage_returnsCustomMessageAndStatus404() {

        var response = messageController.getCustomMessage();

        assertAll(
                () -> assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode()),
                () -> assertNotNull(response.getBody()),
                () -> assertEquals("Message not found in Gold Stack in AWS!", response.getBody().getMessage())
        );
    }
}
