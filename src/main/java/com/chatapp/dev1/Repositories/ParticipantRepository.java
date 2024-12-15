package com.chatapp.dev1.Repositories;

import com.chatapp.dev1.Entities.Chat;
import com.chatapp.dev1.Entities.Participant;
import com.chatapp.dev1.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ParticipantRepository extends JpaRepository<Participant, Long> {
    List<Participant> findAllByUser(User user);

    int countParticipantByChat(Chat chat);

    boolean existsByChatChatIdAndUserUserId(Long chatId, Long userId);
}
