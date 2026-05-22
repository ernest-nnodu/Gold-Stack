package com.jackalcode.gold_stack.service;

import com.jackalcode.gold_stack.dto.MessageResponse;
import com.jackalcode.gold_stack.entity.Message;

import java.util.List;

public interface MessageService {

    List<MessageResponse> getAllMessages();

    MessageResponse getMessage(Long id);

    MessageResponse createMessage(Message message);
}
