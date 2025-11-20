package com.example.authentication.controller;

import com.example.authentication.io.Profileresponse;
import com.example.authentication.io.Profilereuest;
import com.example.authentication.service.Profileservice;
import com.example.authentication.service.emailservice;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.web.bind.annotation.*;
@RestController
@RequiredArgsConstructor


public class Profilecontroller{
    @Autowired
private  Profileservice ps;
    @Autowired
    private emailservice es;


   @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public Profileresponse register(@Valid @RequestBody Profilereuest request){
       Profileresponse response= ps.createProfile(request);
       es.sendemail(response.getEmail(),response.getName());
       return response;
   }
   @GetMapping("/")
    public void  happy(){
       System.out.println("hello everyone ");
   }
   @GetMapping("/test")
    public String test(){
       return "Auth is working";
   }
   @GetMapping("/profile")
    public Profileresponse getprofile(@CurrentSecurityContext(expression = "authentication?.name") String email ){
return ps.getfileresponse(email);
   }

}