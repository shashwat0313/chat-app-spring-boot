package com.chatapp.dev1.Controllers;

import com.chatapp.dev1.Entities.APIResponse;
import com.chatapp.dev1.Entities.DTOs.TokenDTO;
import com.chatapp.dev1.Entities.DTOs.UserAuthDTO;
import com.chatapp.dev1.Entities.DTOs.UserLoginDTO;
import com.chatapp.dev1.Entities.DTOs.UserRegistrationDTO;
import com.chatapp.dev1.Services.UserService;
import com.chatapp.dev1.security.JWTUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;

    public AuthController(UserService userService, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JWTUtil jwtUtil) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }


//  returns the jwt in the response if successful
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody UserRegistrationDTO userRegistrationDTO ){
        log.info("Signup hit");
        if(userService.getUserByUsername(userRegistrationDTO.getUsername()) != null){
            return new ResponseEntity<>
                    (new APIResponse(false, "User already exists!", null),
                                        HttpStatus.BAD_REQUEST);
        }

        com.chatapp.dev1.Entities.User newUser = new com.chatapp.dev1.Entities.User(userRegistrationDTO.getUsername(), "USER", passwordEncoder.encode(userRegistrationDTO.getPassword()));

        com.chatapp.dev1.Entities.User savedUser = userService.saveUser(newUser);

        String token = getJwtFromRolesList(newUser.getRoles(), newUser);

        UserAuthDTO userAuthDTO = new UserAuthDTO(savedUser.getUserId(),savedUser.getUsername(), token, "successful login, token sent.");

        return new ResponseEntity<>(userAuthDTO, HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserLoginDTO userLoginDTO){

        log.info("Login hit with username: " + userLoginDTO.getUsername());

        try{
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userLoginDTO.getUsername(), userLoginDTO.getPassword())
            );

            User user = (User) authentication.getPrincipal();

            log.info("username after auth: " + user.getUsername() + " roles: " + user.getPassword());

            com.chatapp.dev1.Entities.User savedUser = userService.getUserByUsername(user.getUsername());

            String token = getJwtFromRolesList(savedUser.getRoles(), savedUser);

            UserAuthDTO userAuthDTO = new UserAuthDTO(savedUser.getUserId(),savedUser.getUsername(), token, "successful login, token sent.");

            return new ResponseEntity<>(userAuthDTO, HttpStatus.OK);

        }catch (Exception e){
            log.info("Exception = " + e);

            return new ResponseEntity<>(new UserAuthDTO(null, null, null, "Some error"), HttpStatus.UNAUTHORIZED);
        }
    }

    private final String getJwtFromRolesList(String rolesString,  com.chatapp.dev1.Entities.User user){
        List<String> roles = Arrays.stream(user.getRoles().split(",")).map(String::trim).toList();

        return jwtUtil.generateToken(user.getUsername(), roles);
    }

}
