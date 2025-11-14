package com.example.authentication.service;

import com.example.authentication.entity.Userentity;
import com.example.authentication.repio.Userrep;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class Appuserdetails implements UserDetailsService {

    @Autowired
    private Userrep ur;

    // Spring will call THIS method automatically
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Userentity us = ur.findByEmail(email).orElse(null);

        if (us == null) {
            throw new UsernameNotFoundException("Email not found: " + email);
        }

        return new User(
                us.getEmail(),
                us.getPassword(),
                new ArrayList<>()
        );
    }

    // Optional helper method (your custom name)
    public UserDetails loadByUserName(String email) {
        return loadUserByUsername(email);
    }
}
