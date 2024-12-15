package com.chatapp.dev1.Services;

import com.chatapp.dev1.Entities.Chat;
import com.chatapp.dev1.Entities.User;
import com.chatapp.dev1.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

// service annotation belongs to spring framework
// indicates to spring that this class is a service layer compnent and should be treated as a spring bean
@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    public User getUserByUserId(Long userId){
        return userRepository.findById(userId).orElse(null);
    }

    public User getUserByUsername(String username){
        return userRepository.findByUsername(username);
    }

    public User saveUser(User user){
        return userRepository.save(user);
    }

    public void deleteUserById(Long userId){
        userRepository.deleteById(userId);
    }

    public boolean existsByUserId(Long userId){
        return userRepository.existsByUserId(userId);
    }
}
