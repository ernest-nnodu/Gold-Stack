package com.jackalcode.gold_stack.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateMessageRequest(

        @NotBlank(message = "Title is required")
        String title,

        @NotBlank(message = "Content is required")
        String content
) {
}
