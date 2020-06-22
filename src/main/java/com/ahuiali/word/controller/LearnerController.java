package com.ahuiali.word.controller;


import com.ahuiali.word.json.JsonBase;
import com.ahuiali.word.json.LearnerJson;
import com.ahuiali.word.pojo.Learner;
import com.ahuiali.word.service.LearnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.Map;

/**
 * Created by shkstart on 2019/10/20
 */

@Controller
@RequestMapping("/learner")
public class LearnerController {

    @Autowired
    private LearnerService learnerService;

    @Autowired
    private JsonBase jsonBase;

    @Autowired
    private LearnerJson learnerJson;

    /**
     * 跳转至个人界面
     * @return
     */
    @RequestMapping("/profile")
    public String gotoProfile(){
        return "/learner/profile";
    }

    /**
     * 注册检测
     * @param learner
     * @param check
     * @return
     */
    @RequestMapping(value = "/register/{check}",produces = "application/json;charset=utf-8;")
    public @ResponseBody LearnerJson registerCheck(@RequestBody Learner learner, @PathVariable("check") String check){
        if("checkEmail".equals(check)){
            learnerJson = learnerService.queryLearnerByEmail(learner.getEmail());
        }else if("checkNickname".equals(check)){
            learnerJson = learnerService.queryLearnerByNickname(learner.getNickname());
        }else{

            learnerJson.setCode(-1);
            learnerJson.setMessage("参数错误！");
        }

        return learnerJson;
    }

    /**
     * 注册
     * @param learner
     * @return
     */
    @RequestMapping(value = "/register",produces = "application/json;charset=utf-8;")
    public @ResponseBody JsonBase  register(@RequestBody Learner learner){
        jsonBase =  learnerService.register(learner);
        return jsonBase;
    }

    /**
     * 邮箱注册检验
     * @param token
     * @return
     */
    @RequestMapping(value = "/register/confirm/{token}")
    public @ResponseBody JsonBase confirm(@PathVariable("token") String token){
        jsonBase =learnerService.confirm(token);
        return jsonBase;

    }

    //登陆
    @RequestMapping(value = "/login/{isRemember}", produces = "application/json;charset=utf-8;")
    public @ResponseBody JsonBase login(@RequestBody Learner learner,
                                        @PathVariable("isRemember") Integer isRemember,
                                        HttpSession session){
        //根据邮箱和密码查询
        learnerJson =  learnerService.queryLearner(learner);
        if(learnerJson.getCode() == 200){
            session.setAttribute("learnerId",learnerJson.getLearner().getId());
            //七天有效
            if(isRemember == 1){
                session.setMaxInactiveInterval(7*24*60);
            }

        }
        return learnerJson;
    }

    /**
     * 跳转登录
     * @return
     */
    @RequestMapping("/gotoLogin")
    public String gotoLogin(){
        return "/learner/login";
    }

    /**
     * 跳转注册
     * @return
     */
    @RequestMapping("/gotoRegister")
    public String gotoRegister(){
        return "/learner/register";
    }

    /**
     *
     * 跳转找回密码
     * @return
     */
    @RequestMapping("/gotoFindPsw")
    public String gotoFindPsw(){
        return "/learner/resetPassword";
    }

    /**
     * 重新发送邮箱
     * @param learner
     * @return
     */
    @RequestMapping(value = "/login/sentEmailAgain", produces = "application/json;charset=utf-8;")
    public @ResponseBody JsonBase sentEmailAgain(@RequestBody Learner learner){

        jsonBase = learnerService.sentEmailAgain(learner.getEmail());
        System.out.println(jsonBase);
        return jsonBase;
    }

    /**
     * 找回密码
     * @param learner
     * @return
     */
    @RequestMapping(value = "/findPassword",produces = "application/json;charset=utf-8;")
    public @ResponseBody JsonBase findPassword(@RequestBody Learner learner){
            jsonBase = learnerService.findPassword(learner.getEmail());
        return jsonBase;
    }

    /**
     * 修改密码
     * @param learner
     * @return
     */
    @RequestMapping(value = "/updatePassword",produces = "application/json;charset=utf-8;")
    public @ResponseBody JsonBase updatePassword(@RequestBody Learner learner){
        jsonBase = learnerService.updatePassword(learner.getEmail(),learner.getPassword());
        return jsonBase;
    }

    /**
     * 退出登录
     * @param session
     * @return
     */
    @RequestMapping("/loginOut")
    public String loginOut(HttpSession session){
        session.removeAttribute("learnerId");
        return "/learner/login";
    }
}
