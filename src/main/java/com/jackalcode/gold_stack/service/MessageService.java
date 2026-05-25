package com.jackalcode.gold_stack.service;

import com.jackalcode.gold_stack.dto.CreateMessageRequest;
import com.jackalcode.gold_stack.dto.MessageResponse;

import java.util.List;

public interface MessageService {

    List<MessageResponse> getMessages();

    MessageResponse getMessage(Long id);

    MessageResponse createMessage(CreateMessageRequest messageRequest);
}
