package com.melashvili.dataconsistency.services;

import com.melashvili.dataconsistency.events.UserCreationFailedEvent;
import com.melashvili.dataconsistency.model.entities.User;
import com.melashvili.dataconsistency.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private UserRepository userRepository;

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void save(User user) {
        userRepository.save(user);
    }

    @EventListener
    public void handleUserCreationFailed(UserCreationFailedEvent event) {
        Integer userId = event.getUserId();
        userRepository.deleteById(userId);
        System.out.println("Rolling back User with ID: " + userId);
    }
}
