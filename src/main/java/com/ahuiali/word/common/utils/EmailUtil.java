package com.ahuiali.word.common.utils;

import com.ahuiali.word.common.Constant;
import com.ahuiali.word.common.resp.Response;
import com.ahuiali.word.json.JsonBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Component
public class EmailUtil {

    @Autowired
    private static JavaMailSender javaMailSender;

    public static Response<?> sentEmail(String from, String to, String title, String msg){

        try{
            // MimeMessage可以显示html效果
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setFrom(from);
            mimeMessageHelper.setTo(to);
            mimeMessageHelper.setSubject(title);
            mimeMessageHelper.setText(msg, true);
            javaMailSender.send(mimeMessage);
        }catch (MessagingException e){
            e.printStackTrace();
            //出错，邮箱发送失败
            return Response.result(Constant.Error.EMAIL_SEND_ERROR);
        }
        //成功，返回200
        return Response.success();

    }

}
