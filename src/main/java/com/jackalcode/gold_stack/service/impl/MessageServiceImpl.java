package com.jackalcode.gold_stack.service.impl;

import com.jackalcode.gold_stack.dto.CreateMessageRequest;
import com.jackalcode.gold_stack.dto.MessageResponse;
import com.jackalcode.gold_stack.entity.Message;
import com.jackalcode.gold_stack.exception.MessageNotFoundException;
import com.jackalcode.gold_stack.repository.MessageRepository;
import com.jackalcode.gold_stack.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;
    private final MessageIngestionService messageIngestionService;

    @Override
    public List<MessageResponse> getMessages() {

        List<Message> messages = messageRepository.findAll();

        return messages.stream()
                .map(this::mapToMessageResponse)
                .toList();
    }

    @Override
    public MessageResponse getMessage(Long messageId) {

        Message retrievedMessage = getMessageEntity(messageId);

        return mapToMessageResponse(retrievedMessage);
    }

    @Override
    @Transactional
    public MessageResponse createMessage(CreateMessageRequest messageRequest) {

        Message messageToPersist = mapToMessage(messageRequest);

        var persistedMessage = messageRepository.save(messageToPersist);

        messageIngestionService.ingest(persistedMessage);

        return mapToMessageResponse(persistedMessage);
    }

    @Override
    @Transactional
    public MessageResponse updateMessage(Long messageId, CreateMessageRequest messageRequest) {

        Message existingMessage = getMessageEntity(messageId);

        existingMessage.setTitle(messageRequest.title());
        existingMessage.setContent(messageRequest.content());

        var updatedMessage = messageRepository.save(existingMessage);

        messageIngestionService.reIngest(updatedMessage);

        return mapToMessageResponse(updatedMessage);
    }

    @Override
    @Transactional
    public void deleteMessage(Long messageId) {

        Message message = getMessageEntity(messageId);
        messageRepository.delete(message);
        messageIngestionService.delete(messageId);
    }

    private Message getMessageEntity(Long messageId) {
        return messageRepository.findById(messageId)
                .orElseThrow(() -> new MessageNotFoundException(messageId));
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
