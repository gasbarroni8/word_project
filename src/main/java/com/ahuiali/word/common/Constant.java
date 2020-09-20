package com.ahuiali.word.common;

import com.ahuiali.word.common.resp.Result;

/**
 * 常量类
 * @author ZhengChaoHui
 * @Date 2020/9/19 22:27
 */
public class Constant {

    Result SUCCESS = new Result("200", "success");

    public interface Error {
        // 公共
        Result ARG_ERROR = new Result("-1", "参数错误");

        // 登录模块
        Result NICKNAME_EXIST = new Result("401", "昵称已存在");
        Result ACTIVE_EXPIRED = new Result("402", "激活过期");
        Result ACTIVE_INVALID = new Result("403", "激活码无效");
        Result LEARNER_BLOCKED = new Result("404", "用户封禁中");
        Result LEARNER_NON_EMAIL_VERIFY = new Result("405", "用户未完成邮箱验证");
        Result LEARNER_NO_FOUNDED = new Result("406", "找不到用户");
        Result EMAIL_NO_FOUNDED = new Result("407", "邮箱不存在");
        Result EMAIL_EXIST = new Result("408", "邮箱已存在");
        Result EMAIL_SEND_ERROR = new Result("409", "邮箱发送失败");
        Result UPDATE_PWD_ERROR = new Result("410", "更改密码失败");
        Result BLOCK_WORD = new Result("444", "敏感词汇");

        // 单词模块
        Result WORDBOOK_NOT_FOUNDED = new Result("500", "找不到词书");
        Result ADD_WORDBOOK_ERROR = new Result("501", "用户添加词书失败");
        Result LEARNER_NOT_WORDBOOK = new Result("502", "用户无词书");
        Result SET_NEW_WORDBOOK_PLAN_ERROR = new Result("503", "用户设置词书计划失败");
        Result WORDBOOK_EMPTY = new Result("504", "词书单词为空");
        Result WORD_BATCH_INSERT_ERROR = new Result("505", "新词批量保存失败");
        Result WORDBOOK_WORD_NOT_ALL_UPDATE = new Result("506", "记忆表中的单词没有全部更新成功");
        Result NO_REVIEW_WORD = new Result("507", "没有需要复习的单词");

        // 700
        Result WORD_PRE_NOT_FOUNDED = new Result("700", "该单词模糊查询无结果");


    }
}
