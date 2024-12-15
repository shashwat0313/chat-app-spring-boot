    package com.chatapp.dev1.Entities;

    import jakarta.persistence.*;
    import lombok.*;

    @Entity
    @Table(name = "users")
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public class User {

    //  since the Id annotation has been given, the name "user_id" should
    //  not matter
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY) // `SERIAL` primary key
        @Column(name = "user_id") // Maps to the "user_id" column
        private Long userId;

        @Column(name = "username", nullable = false, unique = true, length = 50) // Maps to the "username" column
        private String username;
    }
