package com.treadingPlatformApplication.service.implement;

import com.treadingPlatformApplication.exception.ResourceNotFoundException;
import com.treadingPlatformApplication.models.User;
import com.treadingPlatformApplication.repositories.UserRepository;
import com.treadingPlatformApplication.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImple implements UserService {

    @Autowired
    private UserRepository userRepository;


    @Override
    public User findByUserEmail(String email) {
        return this.userRepository.findByEmail(email);
    }

    @Override
    public User saveUser(User user) {
        return this.userRepository.save(user);
    }
}
