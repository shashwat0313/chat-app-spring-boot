package com.chatapp.dev1.Entities;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageDTO {
    public Long senderUserId;
    public Long chatId;
    public String message;
}
