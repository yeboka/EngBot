package com.yerbo.engbot.services;

import com.yerbo.engbot.models.User;
import com.yerbo.engbot.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User saveUser(Long chatId, String username) {
        User user = null;
        if (userRepository.existsById(chatId)) {
            user = userRepository.findById(chatId).orElse(null);
        } else {
            user = User.builder()
                    .id(chatId)
                    .username(username)
                    .build();

            userRepository.save(user);
        }

        return user;
    }
}
