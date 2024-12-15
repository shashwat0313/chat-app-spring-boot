package com.chatapp.dev1.Controllers;


import com.chatapp.dev1.Entities.APIResponse;
import com.chatapp.dev1.Entities.User;
import com.chatapp.dev1.Entities.UserLoginDTO;
import com.chatapp.dev1.Entities.UserRegistrationDTO;
import com.chatapp.dev1.Services.UserService;
import com.chatapp.dev1.security.JWTUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody UserRegistrationDTO userRegistrationDTO ){
        log.info("Signup hit");
        if(userService.getUserByUsername(userRegistrationDTO.getUsername()) != null){
            return new ResponseEntity<>
                    (new APIResponse(false, "User already exists!", null),
                                        HttpStatus.BAD_REQUEST);
        }

        User newUser = new User(userRegistrationDTO.getUsername(), "USER", passwordEncoder.encode(userRegistrationDTO.getPassword()));

        userService.saveUser(newUser);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(UserLoginDTO userLoginDTO){
        try{
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userLoginDTO.getUsername(), userLoginDTO.getPassword())
            );
        }catch (Exception e){

        }
    }

}
