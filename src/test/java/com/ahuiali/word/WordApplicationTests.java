package com.ahuiali.word;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
class WordApplicationTests {

    @Autowired
    private JavaMailSender javaMailSender;

    @Test
    void contextLoads() {
    }

    @Test
    public void email(){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("1170782807@qq.com");
        message.setTo("15900135325@163.com");
        //生成验证码
        //发送验证码

        message.setSubject("主题：简单邮件");
        message.setText("测试邮件内容");
        javaMailSender.send(message);

        //验证码校验

        //修改用户状态
    }
}
