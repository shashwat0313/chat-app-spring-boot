package com.chatapp.dev1.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "participants")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Participant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // `SERIAL` primary key
    @Column(name = "participant_id") // Maps to the "user_id" column
    private Long participantId;

//    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER) // this field will only be fetched when actually requested
    @JoinColumn(name = "chat_id", nullable = false) // referencedColumnName is not needed since it assumes that the provided field is a PK in the referenced table
    private Chat chat;

//    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}

//What is a Many-to-One Relationship?
//A many-to-one relationship means:
//
//Many rows in the current table (e.g., Participants) are linked to one row in another table (e.g., Conversations or Users).
