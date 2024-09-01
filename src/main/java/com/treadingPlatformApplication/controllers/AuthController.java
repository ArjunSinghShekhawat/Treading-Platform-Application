package com.treadingPlatformApplication.controllers;
import com.treadingPlatformApplication.config.jwt.JwtProvider;
import com.treadingPlatformApplication.models.TwoFactorOtp;
import com.treadingPlatformApplication.models.User;
import com.treadingPlatformApplication.responce.AuthResponce;
import com.treadingPlatformApplication.service.TwoFactorOtpService;
import com.treadingPlatformApplication.service.UserService;
import com.treadingPlatformApplication.service.WatchListService;
import com.treadingPlatformApplication.service.implement.CustomeUserDetails;
import com.treadingPlatformApplication.service.implement.EmailService;
import com.treadingPlatformApplication.utils.OtpUtils;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private EmailService emailService;

    @Autowired
    private TwoFactorOtpService twoFactorOtpService;

    @Autowired
    private UserService userService;

    @Autowired
    private CustomeUserDetails customeUserDetails;

    @Autowired
    private WatchListService watchListService;


    //sign up controller
    @PostMapping("/signup")
    public ResponseEntity<AuthResponce>signup(@RequestBody User user){

        User existsUser = this.userService.findByUserEmail(user.getEmail());

        //check user already present or not
        if(existsUser!=null){
            throw new UsernameNotFoundException(String.format("Email %s is already exists please try with another email !",user.getEmail()));
        }

        //not exists then create user
        User newUser = new User();

        newUser.setFullName(user.getFullName());
        newUser.setUserRole(user.getUserRole());
        newUser.setPassword(user.getPassword());
        newUser.setEmail(user.getEmail());

        //new user saved successfully
        User savedUser = this.userService.saveUser(user);

        this.watchListService.createWatchList(savedUser);

        //jwt token creation
        Authentication authentication = new UsernamePasswordAuthenticationToken(user.getEmail(),user.getPassword());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = JwtProvider.generateToken(authentication);

        //send response for user side
        AuthResponce authResponce = new AuthResponce();
        authResponce.setJwt(jwt);
        authResponce.setMessage(String.format("%s You are successfully register in india one treading platform grow your life",savedUser.getFullName()));
        authResponce.setStatus(true);

        return new ResponseEntity<>(authResponce, HttpStatus.CREATED);

    }
    @PostMapping("/login")
    public ResponseEntity<?>login(@RequestBody User user) throws MessagingException {

        //fetch user details
        String userName = user.getEmail();
        String password = user.getPassword();

        //jwt token creation
        Authentication authentication = authenticate(userName,password);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = JwtProvider.generateToken(authentication);

        //authentication after get user
        User authUser = this.userService.findByUserEmail(userName);

        if(authUser.getTwoFactorAuth().isEnabled()){

            AuthResponce authResponce = new AuthResponce();
            authResponce.setMessage(String.format("%s Your Two Factor Authentication is enable for provide more security of your account",authUser.getFullName()));
            authResponce.setTwoFactorAuthEnabled(true);

            StringBuilder otp = OtpUtils.generateOTP();

            TwoFactorOtp oldTwoFactorOtp = this.twoFactorOtpService.findByUserId(authUser.getId());
            if(oldTwoFactorOtp!=null){
                this.twoFactorOtpService.deleteTwoFactorOtp(oldTwoFactorOtp);
            }
            TwoFactorOtp newTwoFactorOtp = this.twoFactorOtpService.createTwoFactorOtp(authUser,String.valueOf(otp),jwt);

            this.emailService.sendVerificationOtpEmail(authUser.getEmail(),String.valueOf(otp));

            authResponce.setSession(newTwoFactorOtp.getId());

            return new ResponseEntity<>(authResponce,HttpStatus.OK);
        }

        AuthResponce authResponce = new AuthResponce();
        authResponce.setJwt(jwt);
        authResponce.setStatus(true);
        authResponce.setMessage(String.format("%s  You are successfully login in india best treading platform",authUser.getFullName()));

        return new ResponseEntity<>(authResponce,HttpStatus.OK);
    }

    private Authentication authenticate(String userName, String password) {

        UserDetails userDetails = this.customeUserDetails.loadUserByUsername(userName);

        if(userDetails==null){
            throw new BadCredentialsException("Invalid Username");
        }
        else if(!password.equals(userDetails.getPassword())){
            throw new BadCredentialsException("Incorrect password");
        }
        return new UsernamePasswordAuthenticationToken(userName,password);

    }

}
