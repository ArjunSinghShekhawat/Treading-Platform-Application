package com.treadingPlatformApplication.service;

import com.treadingPlatformApplication.models.User;

public interface UserService {
    User findByUserEmail(String email);
    User saveUser(User user);

}
