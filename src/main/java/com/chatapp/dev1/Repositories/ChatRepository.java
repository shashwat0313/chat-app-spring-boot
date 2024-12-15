package com.chatapp.dev1.Repositories;

import com.chatapp.dev1.Entities.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRepository extends JpaRepository<Chat, Long> {
}
