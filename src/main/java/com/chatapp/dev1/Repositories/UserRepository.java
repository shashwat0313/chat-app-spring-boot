package com.chatapp.dev1.Repositories;

import com.chatapp.dev1.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);

    boolean existsByUserId(Long userId);
}


/*
 NOTES
 The below format can be used to create a custom query if required
 @Query("SELECT u FROM User u WHERE u.username = :username")
 User findUserByUsername(@Param("username") String username);
*/

/*
1. **Method Naming Conventions:**
    - The method name `findUserByUsername` adheres to the Spring Data JPA convention for query derivation:
        - `find`: Indicates that a SELECT query should be run.
        - `By`: Signals the start of the condition.
        - `Username`: Refers to the `username` field in your mapped `User` entity.

    - Spring Data JPA maps the `username` in the derived query to the corresponding column in the `User` entity (mapped to the database table).
 */