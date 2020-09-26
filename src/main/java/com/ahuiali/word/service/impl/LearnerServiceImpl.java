package com.ahuiali.word.service.impl;

import com.ahuiali.word.common.Constant;
import com.ahuiali.word.common.resp.Response;
import com.ahuiali.word.mapper.LearnerMapper;
import com.ahuiali.word.pojo.Learner;
import com.ahuiali.word.service.LearnerService;
import com.ahuiali.word.common.utils.Md5Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
 *
 */
@Transactional
@Service
@Slf4j
public class LearnerServiceImpl implements LearnerService {

    @Autowired
    LearnerMapper learnerMapper;

    @Autowired
    JavaMailSender javaMailSender;

    @Override
    public Response<?> addLearner(Learner learner) {
        log.info("添加用户，learner：{}", learner);
        if (!"".equals(learner.getNickname()) && learner.getNickname() != null
                && !"".equals(learner.getEmail()) && learner.getEmail() != null
                && !"".equals(learner.getPassword()) && learner.getPassword() != null)
            learnerMapper.addLearner(learner);

        return Response.success();
    }

    /**
     * 根据邮箱和密码查询用户
     *
     * @param learner1
     * @return
     */
    @Override
    public Response<?> queryLearner(Learner learner1) {
        log.info("根据邮箱和密码查询用户：learner:{}", learner1);
        //md5加密
        learner1.setPassword(Md5Utils.md5(learner1.getPassword()));
        //查询
        Learner learner = learnerMapper.queryLearner(learner1);
        Response<Learner> response = Response.success();
        //该用户存在时
        if (learner != null) {
            Integer status = learner.getStatus();
            if (status == 1) {
                response.setData(learner);
                return response;
            } else if (status == 2) {
                //封禁中
                response = Response.result(Constant.Error.LEARNER_BLOCKED);
                return response;
            } else if (status == 0) {
                //审核中
                response = Response.result(Constant.Error.LEARNER_NON_EMAIL_VERIFY);
                return response;
            }
        }
        //用户不存在直接重新返回
        response = Response.result(Constant.Error.LEARNER_NO_FOUNDED);
        return response;
    }

    /**
     * 根据邮箱查询用户
     *
     * @param email
     * @return
     */
    @Override
    public Response<?> queryLearnerByEmail(String email) {
        log.info("根据邮箱查询用户, email:{}", email);
        Response<Learner> response = Response.success();
        Learner learner = learnerMapper.queryLearnerByEmail(email);
        if (learner == null) {
            //如果在数据库中找不到该email
            return Response.result(Constant.Error.EMAIL_NO_FOUNDED);
        } else {
            //如果在数据库中找到该email
            response.setData(learner);
            response.setCode(Constant.Error.EMAIL_EXIST.getCode());
            response.setMessage(Constant.Error.EMAIL_EXIST.getMessage());
            return response;
        }

    }

    /**
     * 根据昵称查询用户
     *
     * @param nickname
     * @return
     */
    @Override
    public Response<?> queryLearnerByNickname(String nickname) {
        log.info("根据昵称查询用户, nickname:{}", nickname);
        Integer count = learnerMapper.queryLearnerByNickname(nickname);
        Response<?> response = Response.success();
        if (count == 0) {
            //如果在数据库中找不到该昵称
            //敏感检测
            if (true) {
                return response;
            } else {
                return Response.result(Constant.Error.BLOCK_WORD);
            }
        } else {
            //如果在数据库中找到该昵称
            response = Response.result(response, Constant.Error.NICKNAME_EXIST);
        }
        return response;
    }

    /**
     * 保存用户，并向用户发送邮箱
     *
     * @param learner
     * @return
     */
    @Override
    public Response<?> register(Learner learner) {
        log.info("用户注册，learner:{}", learner);
        //password加密
        learner.setPassword(Md5Utils.md5(learner.getPassword()));
        //生成token，时间戳+邮箱
        String token = System.currentTimeMillis() + learner.getEmail();
        //设置激活码
        learner.setActivecode(token);
        Response<?> response = addLearner(learner);
        //添加用户
        if (!Constant.SUCCESS.getCode().equals(response.getCode())) {
            return response;
        }
        //发送邮箱
        String title = "注册检测（背词系统）";
        String msg = "<html><body><a href='http://119.23.219.54:80/learner/register/confirm/" +
                token + "'>点击即可确认身份！</a></body></html>";
        return sentEmail(learner.getEmail(), title, msg);
    }

    /**
     * 激活用户
     *
     * @param activecode
     * @return
     */
    @Override
    public Response<?> confirm(String activecode) {
        Response<?> response = Response.success();
        //30分钟失效
        if ((System.currentTimeMillis() - Long.parseLong(activecode.substring(0, 13))) / (1000 * 60) > 30) {
            response = Response.result(response, Constant.Error.ACTIVE_EXPIRED);
        } else {
            if (learnerMapper.haveActive(activecode) != null) {
                //将用户状态设置为可用
                learnerMapper.confirmLearner(activecode);
            } else {
                //找不到激活码,说明无效
                response = Response.result(response, Constant.Error.ACTIVE_INVALID);
            }
        }
        return response;

    }

    /**
     * 重新发送邮箱
     *
     * @param email
     * @return
     */
    @Override
    public Response<?> sentEmailAgain(String email) {
        log.info("重新发送邮箱, email:{}", email);
        //生成token，时间戳+邮箱
        String token = System.currentTimeMillis() + email;
        //查询邮箱是否存在
        Response<?> response = queryLearnerByEmail(email);
        //如果存在该邮箱,408表示邮箱已存在
        if (response.getCode() == "408") {
            //发送邮箱
            String title = "注册检测（背词系统）";
            String msg = "<html><body><a href='http://119.23.219.54:80/learner/register/confirm/" +
                    token + "'>点击即可确认身份！</a></body></html>";
            response = sentEmail(email, title, msg);
            //邮箱发送成功
            if (response.getCode() == "200") {
                //设置激活码
                learnerMapper.setActivecodeByEmail(email, token);
                return response;
            } else if (response.getCode() == "409") {
                //邮箱发送失败
                response = Response.result(response, Constant.Error.EMAIL_SEND_ERROR);
                return response;
            }

        }

        //不存在邮箱
        response = Response.result(response, Constant.Error.EMAIL_NO_FOUNDED);
        return response;


    }

    /**
     * 找回密码
     *
     * @param email
     * @return
     */
    @Override
    public Response<?> findPassword(String email) {
        log.info("找回密码, email:{}", email);
        //该邮箱是否存在
        Response<?> response = queryLearnerByEmail(email);
        //邮箱存在
        if (response.getCode() == "408") {
            String title = "找回密码";
            //设置新密码（MD5不可解密，故只能自动设置一个新的密码，然后返回，用户可根据该密码登陆，然后再修改）
//            String newPassword = learnerJson.getLearner().getPassword().substring(0,7);
            // TODO 可修改为随机字符串
            String newPassword = "12345678";
            //发送邮箱
            response = sentEmail(email, title, "密码默认设置为 ：" + newPassword);
            //邮箱发送成功
            if (Constant.SUCCESS.getCode().equals(response.getCode())) {
                //当邮箱发送成功才修改密码
                learnerMapper.updatePassword(email, Md5Utils.md5(newPassword));
                return response;
            } else if (Constant.Error.EMAIL_SEND_ERROR.getCode().equals(response.getCode())) {
                log.warn("邮箱发送失败, email:{}", email);
                //邮箱发送失败
                response = Response.result(Constant.Error.EMAIL_SEND_ERROR);
                return response;
            }

        }

        //不存在邮箱
        response = Response.result(response, Constant.Error.EMAIL_NO_FOUNDED);
        return response;

    }

    /**
     * 修改密码
     *
     * @param email
     * @param newPassword
     * @return
     */
    @Override
    public Response<?> updatePassword(String email, String newPassword) {
        Integer count = learnerMapper.updatePassword(email, newPassword);
        if (count > 0) {
            return Response.success();
        } else {
            return Response.result(Constant.Error.UPDATE_PWD_ERROR);
        }
    }

    /**
     * 邮箱类
     *
     * @param email 用户邮箱
     * @return
     */
    public Response<?> sentEmail(String email, String title, String msg) {
        try {
            // MimeMessage可以显示html效果
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setFrom("1170782807@qq.com");
            mimeMessageHelper.setTo(email);
            mimeMessageHelper.setSubject(title);
            mimeMessageHelper.setText(msg, true);
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            //出错，邮箱发送失败
            log.error("邮箱发送失败, error:{}", e.toString());
            return Response.result(Constant.Error.EMAIL_SEND_ERROR);
        }
        //成功，返回200
        return Response.success();
    }
}
