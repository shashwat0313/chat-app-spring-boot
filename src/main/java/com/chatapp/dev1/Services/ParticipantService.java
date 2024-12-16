package com.chatapp.dev1.Services;

import com.chatapp.dev1.Entities.Chat;
import com.chatapp.dev1.Entities.Participant;
import com.chatapp.dev1.Entities.User;
import com.chatapp.dev1.Repositories.ParticipantRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ParticipantService {

    private final ParticipantRepository participantRepository;
    private final EntityManager entityManager;


    public ParticipantService(ParticipantRepository participantRepository, EntityManager entityManager) {
        this.participantRepository = participantRepository;
        this.entityManager = entityManager;
    }

    public List<Participant> getAllParticipants(){
        return participantRepository.findAll();
    }

    public Participant getParticipantById(Long participantId){
        return participantRepository.findById(participantId).orElse(null);
    }

    public Participant saveParticipant(Participant participant){
        return participantRepository.save(participant);
    }

    public void deleteParticipantById(Long participantId){
        participantRepository.deleteById(participantId);
    }

    public List<Participant> getParticipantsByUserId(User user) {
        return participantRepository.findAllByUser(user);

    }

    public Chat getReferenceToChat(Long chatId) {
        return entityManager.getReference(Chat.class, chatId);
    }

    public User getReferenceToUser(Long userId) {
        return entityManager.getReference(User.class, userId);
    }

    public int getUserCountByChatRef(Chat chat) {
        return participantRepository.countParticipantByChat(chat);
    }

    public boolean participantExistsByChatRefAndUserRef(Chat chat, User user) {
        return participantRepository.existsByChatChatIdAndUserUserId(chat.getChatId(), user.getUserId());
    }

    public void deleteParticipantByUserId(Long userId) {
        participantRepository.deleteParticipantByUser_UserId(userId);
    }

    public List<Participant> getParticipantsByChatId(Long chatId) {
        return participantRepository.getParticipantsByChat_ChatId(chatId);
    }

//    @Transactional
//    public Participant getFullParticipantDetails(Long participantId) {
//        Participant participant = participantRepository.findById(participantId)
//                .orElseThrow(() -> new EntityNotFoundException("Participant not found"));
//
//        // Initialize lazy-loaded associations
//        participant.getChat().getChatName();
//        participant.getUser().getUsername();
//
//        return participant;
//    }
}
