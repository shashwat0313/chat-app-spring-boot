package com.chatapp.dev1.security;

import com.chatapp.dev1.Services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CustomUserDetailsServiceImpl implements UserDetailsService {

    private final UserService userService;

    public CustomUserDetailsServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {

//        log.info("username(in customuserde...impl)=" + username);

        com.chatapp.dev1.Entities.User user = userService.getUserByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("User: " + username + " not found.");
        }

//        log.info("user roles = " + user.getRoles());

        List<SimpleGrantedAuthority> authorities = Arrays.stream(user.getRoles().split(","))
                .map(role ->
                        new SimpleGrantedAuthority("ROLE_" + role.trim().toUpperCase()))
                .toList();

        return new User(
                user.getUsername(),
                user.getEncodedPassword(),
                authorities
        );
    }
}
