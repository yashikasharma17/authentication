package com.example.authentication.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor

public class emailservice {
    @Autowired
    private  JavaMailSender javaMailSender;


    @Value("${spring.mail.properties.mail.smtp.from}")
    public String emailfrom;
    public void sendemail(String toemail,String name){
        SimpleMailMessage message=new SimpleMailMessage();
        message.setFrom(emailfrom);
        message.setTo(toemail);
        message.setSubject("Welcome to our page");
        message.setText("Hello "+name+"\n\n Thanks for registering with us ,\n\n Regards, \n Zukiyo Team");
        javaMailSender.send(message);
    }
}

