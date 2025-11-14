package com.example.authentication.controller;

import com.example.authentication.io.Profileresponse;
import com.example.authentication.io.Profilereuest;
import com.example.authentication.service.Profileservice;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1.0")

public class Profilecontroller{
    @Autowired
private  Profileservice ps;


   @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public Profileresponse register(@Valid @RequestBody Profilereuest request){
       Profileresponse response= ps.createProfile(request);
       return response;
   }

}