package com.jackalcode.gold_stack.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;

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

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().content()).isEqualTo("Hello From Gold Stack in AWS!");
    }

    @Test
    @DisplayName("getErrorMessage should return error message and status 502")
    public void getErrorMessage_returnsErrorMessageAndStatus502() {

        var response = messageController.getErrorMessage();

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_GATEWAY);
        assertThat(response.getBody().content()).isEqualTo("This is an error message from Gold Stack in AWS!");
    }

    @Test
    @DisplayName("getCustomMessage should return custom message and status 404")
    public void getCustomMessage_returnsCustomMessageAndStatus404() {

        var response = messageController.getCustomMessage();

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody().content()).isEqualTo("Message not found in Gold Stack in AWS!");
    }
}
