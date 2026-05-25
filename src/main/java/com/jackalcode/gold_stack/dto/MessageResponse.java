package com.jackalcode.gold_stack.dto;

public record MessageResponse(
        Long id,
        String title,
        String content,
        String createdAt
) {
}
