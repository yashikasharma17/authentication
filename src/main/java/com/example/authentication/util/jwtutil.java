package com.example.authentication.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

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
                .setExpiration(new Date(System.currentTimeMillis()+1000*60*60*24*7))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setSubject(username).signWith(SignatureAlgorithm.HS256,secret_key)
                .compact();
    }
    public Claims extractAllClaims(String token){
        return Jwts.parser()
                .setSigningKey(secret_key)

                .parseClaimsJws(token)
                .getBody();
    }
public <T> T extractAllClaims(String token, Function<Claims,T> claimsResolver){
final Claims claims=extractAllClaims(token);
return claimsResolver.apply(claims);
}
public String extractEmails(String token){
        return extractAllClaims(token,Claims::getSubject);
}
public Date extractExpiration(String token){
        return extractAllClaims(token,Claims::getExpiration);
}
public Boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());

}
public boolean validateToken(String token,UserDetails ud){
        final String email=extractEmails(token);
        return (email.equals(ud.getUsername()) && !isTokenExpired(token));
}
}
