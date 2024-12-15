package com.chatapp.dev1.Entities;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "chats")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_id")
    private Long chatId;

    @Column(name = "chat_name", length = 100, nullable = false)
    private String chatName;

    @Column(name = "is_group", nullable = false)
    private Boolean isGroup;
}
