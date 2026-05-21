package com.jackalcode.gold_stack.repository;

import com.jackalcode.gold_stack.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {
}
