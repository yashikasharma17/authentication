package com.example.authentication.service;

import com.example.authentication.entity.Userentity;
import com.example.authentication.io.Profileresponse;
import com.example.authentication.io.Profilereuest;
import com.example.authentication.repio.Userrep;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class Profileservice implements Profileserviceimpl {
    private final Userrep ur;

    // manually created constructor
    public Profileservice(Userrep ur) {
        this.ur = ur;
    }

    @Override
    public Profileresponse createProfile(Profilereuest request) {
        Userentity newProfile = convertToEntity(request);
        newProfile = ur.save(newProfile);
        return convertToFileResponse(newProfile);
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
        entity.setPassword(request.getPassword());
        entity.setAccountVerified(false);
        entity.setResetOtpExpireAt(String.valueOf(0L));
        entity.setVerifyOtp(null);
        entity.setVerifyOtpExpired(String.valueOf(0L));
        entity.setResetOtp(null);
        return entity;
    }
}
