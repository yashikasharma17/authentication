package com.example.authentication.service;

import com.example.authentication.entity.Userentity;
import com.example.authentication.io.Profileresponse;
import com.example.authentication.io.Profilereuest;
import com.example.authentication.repio.Userrep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class Profileservice implements Profileserviceimpl {
    @Autowired
    private final Userrep ur;
    @Autowired
    private final PasswordEncoder pe;
    @Autowired
    private final emailservice es;

    public Profileservice(Userrep ur, PasswordEncoder pe, emailservice es) {
        this.ur = ur;
        this.pe = pe;
        this.es = es;
    }

    // manually created constructor

    @Override
    public Profileresponse createProfile(Profilereuest request) {
        Userentity newProfile = convertToEntity(request);
        if(!ur.findByEmail(request.getEmail()).isPresent()){
            newProfile = ur.save(newProfile);
            return convertToFileResponse(newProfile);
        }
        throw new ResponseStatusException(HttpStatus.CONFLICT,"Email already exists ");
    }

    @Override
    public Profileresponse getfileresponse(String email) {
        Userentity user=ur.findByEmail(email)
                .orElseThrow(()-> new UsernameNotFoundException("this email does not exist: "+email));
        return convertToFileResponse(user);

    }

    @Override
    public void sendResendOTP(String email) {
        Userentity userentity=ur.findByEmail(email)
                .orElseThrow(()->new UsernameNotFoundException("cant find the user with"+email));
                String otp= String.valueOf(ThreadLocalRandom.current().nextInt(100000,1000000));
                long expiryTime=System.currentTimeMillis()+(15*60*1000);
                userentity.setResetOtp(otp);
                userentity.setResetOtpExpireAt(expiryTime);
                ur.save(userentity);
                try{
es.sendResendOTP(userentity.getEmail(), otp);
                }catch(Exception e){
                    throw new RuntimeException("Unable to send email");
                }
    }

    @Override
    public void resetPassword(String email, String otp, String password) {
        Userentity userentity=ur.findByEmail(email)
                .orElseThrow(()->new UsernameNotFoundException("cant find the user with"+email));
        if(userentity.getResetOtp()==null || !userentity.getResetOtp().equals(otp)){
            throw new RuntimeException("the otp does not match");
        }
        if(userentity.getResetOtpExpireAt() < System.currentTimeMillis()){
            throw new RuntimeException("OTP expired");
        }
        userentity.setPassword(password);
        userentity.setResetOtp(null);
        userentity.setResetOtpExpireAt(0L);
        ur.save(userentity);
    }

    private Profileresponse convertToFileResponse(Userentity newProfile) {
        Profileresponse response = new Profileresponse();
        response.setName(newProfile.getName());
        response.setEmail(newProfile.getEmail());
        response.setUserid(newProfile.getUserid());
        response.setIsAccountVerified(newProfile.isAccountVerified());
        return response;
    }

    private Userentity convertToEntity(Profilereuest request) {
        Userentity entity = new Userentity();
        entity.setEmail(request.getEmail());
        entity.setUserid(UUID.randomUUID().toString());
        entity.setName(request.getName());
        entity.setPassword(pe.encode(request.getPassword()));
        entity.setAccountVerified(false);
        entity.setResetOtpExpireAt(0L);
        entity.setVerifyOtp(null);
        entity.setVerifyOtpExpired(String.valueOf(0L));
        entity.setResetOtp(null);
        return entity;
    }
}
