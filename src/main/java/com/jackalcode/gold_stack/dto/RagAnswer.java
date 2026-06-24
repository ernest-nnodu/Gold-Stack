package com.jackalcode.gold_stack.dto;

import java.util.List;

public record RagAnswer(
        String answer,
        List<RagSource> sources
) {
}
