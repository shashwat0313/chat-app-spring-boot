package com.chatapp.dev1.Entities.DTOs;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageDTO {
    public Long senderUserId;
    public Long chatId;
    public String message;
}
