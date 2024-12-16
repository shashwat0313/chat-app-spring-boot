package com.chatapp.dev1.Controllers;

import com.chatapp.dev1.Entities.User;
import com.chatapp.dev1.Services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

    @GetMapping("/username")
    public ResponseEntity<User> getUserByUsername(){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        org.springframework.security.core.userdetails.User securityUser =
                (org.springframework.security.core.userdetails.User) authentication.getPrincipal();

        String usernameFromSecurity = securityUser.getUsername();

        log.info("usernameFromSecurity=" + usernameFromSecurity);

        User user = userService.getUserByUsername(usernameFromSecurity);

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
