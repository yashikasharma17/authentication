package com.example.authentication.filter;

import com.example.authentication.service.Appuserdetails;
import com.example.authentication.util.jwtutil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor


public class jwtsRequestFilter extends OncePerRequestFilter {
    @Autowired

    private  Appuserdetails app;
    @Autowired

    private  jwtutil jwtu;


    private final static List<String> PUBLIC_URLS=List.of("/login","/register","/send-resend-otp","/reset-pssword","/logout");


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
       final String path=request.getServletPath();
       if(PUBLIC_URLS.contains(path)){
           filterChain.doFilter(request,response);
           return;
       }
       String email=null;
       String jwt=null;
       final String authorizationheader=request.getHeader("Authorization");
       if(authorizationheader!=null && authorizationheader.startsWith("Bearer ")){
           jwt=authorizationheader.substring(7);
       }
       if(jwt==null){
           Cookie[] cookie= request.getCookies();
           if(cookie!=null){
               for(Cookie cookies:cookie){
                   if("jwt".equals(cookies.getName())){
                       jwt=cookies.getValue();
                       break;
                   }
               }
           }

       }
       if(jwt!=null){
           email=jwtu.extractEmails(jwt);
           if(email!=null && SecurityContextHolder.getContext().getAuthentication()==null){
               UserDetails ud=app.loadUserByUsername(email);
               if(jwtu.validateToken(jwt,ud)){
                   UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken=
                           new UsernamePasswordAuthenticationToken(ud,null, ud.getAuthorities());
                   usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                   SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
               }
           }

       }
        filterChain.doFilter(request,response);

    }
}
