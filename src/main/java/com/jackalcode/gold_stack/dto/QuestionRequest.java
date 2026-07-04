package com.jackalcode.gold_stack.dto;

import jakarta.validation.constraints.NotBlank;

public record QuestionRequest(

        @NotBlank(message = "Question is required")
        String question
) {
}
