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
}
