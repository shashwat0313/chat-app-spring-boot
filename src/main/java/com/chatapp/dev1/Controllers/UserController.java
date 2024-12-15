package com.chatapp.dev1.Controllers;

import com.chatapp.dev1.Entities.User;
import com.chatapp.dev1.Services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Logger;

//@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    private Logger log = Logger.getLogger(UserController.class.getName());

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> getAllUsers(){
        return userService.getAllUsers();
    }

    @GetMapping("/id/{userId}")
    public ResponseEntity<User> getUserByUserId(@PathVariable Long userId){
        User user = userService.getUserByUserId(userId);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<User> getUserByUsername(@PathVariable String username){
        User user = userService.getUserByUsername(username);

        return ResponseEntity.ok(user);
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user){
        log.info("hello");
        return ResponseEntity.ok(userService.saveUser(user));
    }

    @DeleteMapping("/id/{userId}")
    public void deleteUserById(@PathVariable Long userId){
        userService.deleteUserById(userId);
    }

    @PutMapping
    public ResponseEntity<User> updateUser(@RequestBody User user){
        return ResponseEntity.ok(userService.saveUser(user));
    }
}
