package com.treadingPlatformApplication.service.implement;

import com.treadingPlatformApplication.config.jwt.JwtProvider;
import com.treadingPlatformApplication.domain.VerificationType;
import com.treadingPlatformApplication.exception.ResourceNotFoundException;
import com.treadingPlatformApplication.models.TwoFactorAuth;
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

    @Override
    public User findUserProfileByJwt(String jwt) {
        String email = JwtProvider.getEmailFromToken(jwt);
        User user = this.userRepository.findByEmail(email);

        if(user==null){
            throw new ResourceNotFoundException("user","jwt",String.valueOf(jwt));
        }
        return user;
    }

    @Override
    public User findById(Long userId) {
        return this.userRepository.findById(userId).orElseThrow(()->new ResourceNotFoundException("user","userId",String.valueOf(userId)));
    }

    @Override
    public User enableTwoFactorAuthentication(VerificationType verificationType, String sendTo, User user) {
        TwoFactorAuth twoFactorAuth = new TwoFactorAuth();

        twoFactorAuth.setEnabled(true);
        twoFactorAuth.setVerificationType(verificationType);

        user.setTwoFactorAuth(twoFactorAuth);

        return this.userRepository.save(user);
    }

    @Override
    public void updatePassword(User user, String password) {
        user.setPassword(password);
        this.userRepository.save(user);
    }
}
