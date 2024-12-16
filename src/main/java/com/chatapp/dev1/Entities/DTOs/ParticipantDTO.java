package com.chatapp.dev1.Entities.DTOs;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ParticipantDTO {
    private Long userId;
    private Long chatId;

    @Override
    public String toString() {

        return "ParticipantDTO{" + "userId=" + userId + ", chatId=" + chatId + '}';
    }
}
