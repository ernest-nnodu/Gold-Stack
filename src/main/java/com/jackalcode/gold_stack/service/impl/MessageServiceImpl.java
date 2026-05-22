package com.jackalcode.gold_stack.service.impl;

import com.jackalcode.gold_stack.dto.CreateMessageRequest;
import com.jackalcode.gold_stack.dto.MessageResponse;
import com.jackalcode.gold_stack.entity.Message;
import com.jackalcode.gold_stack.exception.MessageNotFoundException;
import com.jackalcode.gold_stack.repository.MessageRepository;
import com.jackalcode.gold_stack.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;

    @Override
    public List<MessageResponse> getAllMessages() {

        List<Message> messages = messageRepository.findAll();

        return messages.stream()
                .map(this::mapToMessageResponse)
                .toList();
    }

    @Override
    public MessageResponse getMessage(Long id) {

        Message retrievedMessage = messageRepository.findById(id)
                .orElseThrow(() -> new MessageNotFoundException(id));

        return mapToMessageResponse(retrievedMessage);
    }

    @Override
    public MessageResponse createMessage(CreateMessageRequest messageRequest) {

        Message messageToPersist = mapToMessage(messageRequest);

        var persistedMessage = messageRepository.save(messageToPersist);

        return mapToMessageResponse(persistedMessage);
    }

    private Message mapToMessage(CreateMessageRequest messageRequest) {

        return Message.builder()
                .title(messageRequest.title())
                .content(messageRequest.content())
                .build();
    }

    private MessageResponse mapToMessageResponse(Message message) {
        return new MessageResponse(
                message.getId(),
                message.getTitle(),
                message.getContent(),
                message.getCreatedAt().toString()
        );
    }
}
