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

@Service
public class Profileservice implements Profileserviceimpl {
    @Autowired
    private final Userrep ur;
    @Autowired
    private final PasswordEncoder pe;

    // manually created constructor
    public Profileservice(Userrep ur, PasswordEncoder pe) {
        this.ur = ur;
        this.pe = pe;
    }

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
        entity.setResetOtpExpireAt(String.valueOf(0L));
        entity.setVerifyOtp(null);
        entity.setVerifyOtpExpired(String.valueOf(0L));
        entity.setResetOtp(null);
        return entity;
    }
}
