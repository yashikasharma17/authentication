package com.example.authentication.service;

import com.example.authentication.entity.Userentity;
import com.example.authentication.repio.Userrep;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class Appuserdetails {
    @Autowired
    private Userrep ur;
    public UserDetails loadByUserName(String email) throws UsernameNotFoundException{

        Userentity us;
        us = ur.findByEmail(email).orElse(null);

        if (us == null) {
            throw new UsernameNotFoundException("Email not found for: " + email);
        }

        return new User(us.getEmail(),us.getPassword(),new ArrayList<>());
    }

}
