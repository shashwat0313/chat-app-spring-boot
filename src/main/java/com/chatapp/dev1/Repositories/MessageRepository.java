package com.chatapp.dev1.Repositories;

import com.chatapp.dev1.Entities.Chat;
import com.chatapp.dev1.Entities.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findAllByChat(Chat chat);
}
