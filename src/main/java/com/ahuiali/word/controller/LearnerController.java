package com.ahuiali.word.controller;


import com.ahuiali.word.common.Constant;
import com.ahuiali.word.common.resp.Response;
import com.ahuiali.word.common.resp.Result;
import com.ahuiali.word.pojo.Learner;
import com.ahuiali.word.service.LearnerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;


/**
 * Created by shkstart on 2019/10/20
 */

@Controller
@RequestMapping("/learner")
@Slf4j
public class LearnerController {

    @Autowired
    private LearnerService learnerService;

    /**
     * 跳转至个人界面
     *
     * @return
     */
    @RequestMapping("/profile")
    public String gotoProfile() {
        return "/learner/profile";
    }

    /**
     * 注册检测
     *
     * @param learner
     * @param check
     * @return
     */
    @RequestMapping(value = "/register/{check}", produces = "application/json;charset=utf-8;")
    public @ResponseBody
    Response<?> registerCheck(@RequestBody Learner learner, @PathVariable("check") String check) {
        Response<?> response = null;
        if ("checkEmail".equals(check)) {
            response = learnerService.queryLearnerByEmail(learner.getEmail());
        } else if ("checkNickname".equals(check)) {
            response = learnerService.queryLearnerByNickname(learner.getNickname());
        } else {
            response = Response.result(Constant.Error.ARG_ERROR);
        }
        return response;
    }

    /**
     * 注册
     *
     * @param learner
     * @return
     */
    @RequestMapping(value = "/register", produces = "application/json;charset=utf-8;")
    public @ResponseBody
    Response<?> register(@RequestBody Learner learner) {
        return learnerService.register(learner);
    }

    /**
     * 邮箱注册检验
     *
     * @param token
     * @return
     */
    @RequestMapping(value = "/register/confirm/{token}")
    public String confirm(@PathVariable("token") String token) {
        Response<?> response = learnerService.confirm(token);
        String code = response.getCode();
        if (Constant.SUCCESS.getCode().equals(code)) {
            return "success";
        } else if (Constant.Error.ACTIVE_EXPIRED.getCode().equals(code)) {
            // TODO 还没解决重新发送邮箱的功能
            // 激活码过期，请重新发送邮箱
            return "expired";
        } else if (Constant.Error.ACTIVE_INVALID.getCode().equals(code)) {
            // 激活码无效
            return "invalid";
        }
        return "index";
    }

    /**
     * 登陆
     */
    @RequestMapping(value = "/login/{isRemember}", produces = "application/json;charset=utf-8;")
    public @ResponseBody
    Response<?> login(@RequestBody Learner learner,
                      @PathVariable("isRemember") Integer isRemember,
                      HttpSession session) {
        log.info("用户登录, learnerId:{}", learner.getEmail());
        //根据邮箱和密码查询
        Response<Learner> response = (Response<Learner>) learnerService.queryLearner(learner);
        if ("200".equals(response.getCode())) {
            session.setAttribute("learnerId", response.getData().getId());
            //七天有效
            if (isRemember == 1) {
                session.setMaxInactiveInterval(7 * 24 * 60);
            }
        }
        return response;
    }

    /**
     * 跳转登录
     *
     * @return
     */
    @RequestMapping("/gotoLogin")
    public String gotoLogin() {
        return "/learner/login";
    }

    /**
     * 跳转注册
     *
     * @return
     */
    @RequestMapping("/gotoRegister")
    public String gotoRegister() {
        return "/learner/register";
    }

    /**
     * 跳转找回密码
     *
     * @return
     */
    @RequestMapping("/gotoFindPsw")
    public String gotoFindPsw() {
        return "/learner/resetPassword";
    }

    /**
     * 重新发送邮箱
     *
     * @param learner
     * @return
     */
    @RequestMapping(value = "/login/sentEmailAgain", produces = "application/json;charset=utf-8;")
    public @ResponseBody
    Response<?> sentEmailAgain(@RequestBody Learner learner) {

        Response<?> response = learnerService.sentEmailAgain(learner.getEmail());
        return response;
    }

    /**
     * 找回密码
     *
     * @param learner
     * @return
     */
    @RequestMapping(value = "/findPassword", produces = "application/json;charset=utf-8;")
    public @ResponseBody
    Response<?> findPassword(@RequestBody Learner learner) {
        return learnerService.findPassword(learner.getEmail());
    }

    /**
     * 修改密码
     *
     * @param learner
     * @return
     */
    @RequestMapping(value = "/updatePassword", produces = "application/json;charset=utf-8;")
    public @ResponseBody
    Response<?> updatePassword(@RequestBody Learner learner) {
        return learnerService.updatePassword(learner.getEmail(), learner.getPassword());
    }

    /**
     * 退出登录
     *
     * @param session
     * @return
     */
    @RequestMapping("/loginOut")
    public String loginOut(HttpSession session) {
        session.removeAttribute("learnerId");
        return "/learner/login";
    }
}
