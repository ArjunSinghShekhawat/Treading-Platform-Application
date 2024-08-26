package com.treadingPlatformApplication.utils;

import java.util.Random;

public class OtpUtils {

    public static StringBuilder generateOTP(){
        int lengthOfOtp = 6;

        StringBuilder otp = new StringBuilder(lengthOfOtp);

        Random random = new Random();
        for(int i=0;i<6;i++){
            otp.append(random.nextInt());
        }
        return otp;
    }
}
