package com.example.authentication.service;

import com.example.authentication.io.Profilereuest;
import com.example.authentication.io.Profileresponse;

public interface Profileserviceimpl {
    Profileresponse createProfile(Profilereuest request);
}
