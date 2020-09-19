package com.ahuiali.word.common.utils;

import com.ahuiali.word.json.JsonBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

public class EmailUtil {

    @Autowired
    JavaMailSender javaMailSender;

    public  JsonBase sentEmail(String from, String to, String title ,String msg){
        System.out.println(javaMailSender);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to);

        message.setSubject(title);
        message.setText(msg);
        javaMailSender.send(message);
        return new JsonBase();

    }

}