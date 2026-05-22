package com.jackalcode.gold_stack.service;

import com.jackalcode.gold_stack.dto.CreateMessageRequest;
import com.jackalcode.gold_stack.entity.Message;
import com.jackalcode.gold_stack.exception.MessageNotFoundException;
import com.jackalcode.gold_stack.repository.MessageRepository;
import com.jackalcode.gold_stack.service.impl.MessageServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MessageServiceTest {

    @Mock
    private MessageRepository messageRepository;

    @InjectMocks
    private MessageServiceImpl messageService;

    @Test
    @DisplayName("getMessages should return all messages")
    void getMessages_whenMessagesExists_returnsAllMessages() {

        when(messageRepository.findAll()).thenReturn(List.of(
                createMessage(1L, "Title 1", "Content 1"),
                createMessage(2L, "Title 2", "Content 2")
        ));

        var result = messageService.getMessages();

        assertThat(result).hasSize(2)
                .extracting("id", "title", "content")
                .containsExactlyInAnyOrder(
                        tuple(1L, "Title 1", "Content 1"),
                        tuple(2L, "Title 2", "Content 2")
                );

        verify(messageRepository).findAll();
    }

    @Test
    @DisplayName("getMessages should return empty list when messages does not exist")
    void getMessages_whenMessagesDoesNotExist_returnsEmptyList() {

        when(messageRepository.findAll()).thenReturn(List.of());

        var result = messageService.getMessages();

        assertThat(result).isEmpty();

        verify(messageRepository).findAll();
    }

    @Test
    @DisplayName("getMessage should return message when message exists")
    void getMessage_whenMessageExists_returnsMessage() {
        when(messageRepository.findById(1L))
                .thenReturn(Optional.of(
                        createMessage(1L, "Title 1", "Content 1")));

        var result = messageService.getMessage(1L);

        assertThat(result)
                .isNotNull()
                .extracting("id", "title", "content")
                .containsExactly(1L, "Title 1", "Content 1");

        verify(messageRepository).findById(1L);
    }

    @Test
    @DisplayName("getMessage should throw exception when message does not exist")
    void getMessage_whenMessageDoesNotExist_throwsException() {
        when(messageRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(MessageNotFoundException.class, () -> messageService.getMessage(1L));

        verify(messageRepository).findById(1L);
    }

    @Test
    @DisplayName("createMessage should return created message when message request is valid")
    void createMessage_whenMessageRequestIsValid_returnsCreatedMessage() {

        var persistedMessage = createMessage(1L, "Title 1", "Content 1");

        when(messageRepository.save(any(Message.class))).thenReturn(persistedMessage);

        var result = messageService.createMessage(
                new CreateMessageRequest("Title 1", "Content 1"));

        assertThat(result)
                .isNotNull()
                .extracting("id", "title", "content")
                .containsExactly(1L, "Title 1", "Content 1");

        verify(messageRepository).save(any(Message.class));
    }

    private Message createMessage(Long id, String title, String content) {

        return Message.builder()
                .id(id)
                .title(title)
                .content(content)
                .createdAt(Instant.now())
                .build();
    }
}
