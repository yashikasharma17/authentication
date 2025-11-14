package com.example.authentication.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component

public class jwtutil {
    @Value("${jwt.secret.key}")
    private String secret_key;
    public String generateToken(UserDetails ud){
        Map<String,Object> map=new HashMap<>();
        return createToken(map,ud.getUsername());
    }

    private String createToken(Map<String, Object> map, String username) {
      return  Jwts.builder()
                .setClaims(map)
                .setExpiration(new Date(System.currentTimeMillis()))
                .setIssuedAt(new Date(System.currentTimeMillis()+1000*60*60*10))
                .setSubject(username).signWith(SignatureAlgorithm.HS256,secret_key)
                .compact();
    }
}
