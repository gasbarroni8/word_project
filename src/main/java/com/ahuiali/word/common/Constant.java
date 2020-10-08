package com.ahuiali.word.common;

import com.ahuiali.word.common.resp.Result;

/**
 * 常量类
 * @author ZhengChaoHui
 * @date 2020/9/19 22:27
 */
public class Constant {

    public final static Integer ZERO = 0;

    /**
     * 邮箱类
     */
    public final static String EMAIL_FORM = "1170782807@qq.com";
    public final static String FIND_PWD_TITLE = "找回密码";
    public final static String FIND_PWD_MSG = "密码默认设置为：%s";

    public final static String REGISTER_TITLE = "注册检测（背词系统）";
    public final static String REGISTER_MSG = "<html><body><a href='http://119.23.219.54:80/learner/register/confirm/%s'>点击即可确认身份！</a></body></html>";

    public static Result SUCCESS = new Result("200", "success");

    public interface Error {
        // 公共
        Result ARG_ERROR = new Result("-1", "参数错误");

        // 登录模块
        Result LEARNER_ADD_ERROR = new Result("400", "用户添加失败！");
        Result NICKNAME_EXIST = new Result("401", "昵称已存在");
        Result ACTIVE_EXPIRED = new Result("402", "激活码过期，请重新发送邮箱");
        Result ACTIVE_INVALID = new Result("403", "激活码无效，请重新注册");
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

        // 生词本模块
        Result NOTEBOOK_EMPTY = new Result("600", "生词本列表为空");
        Result NOTEBOOK_ADD_ERROR = new Result("601", "生词本添加失败");
        Result NOTEBOOK_DELETE_ERROR = new Result("602", "生词本删除失败");
        Result NOTEBOOK_WORD_DELETE_ERROR = new Result("603", "生词本单词删除失败");
        Result NOTEBOOK_WORD_ADD_ERROR = new Result("604", "生词本单词添加失败");
        Result NOTEBOOK_WORD_EMPTY = new Result("605", "生词本单词为空");
        Result NOTEBOOK_UPDATE_ERROR = new Result("606", "生词本修改失败");

        // 700
        Result WORD_PRE_NOT_FOUNDED = new Result("700", "该单词模糊查询无结果");
        Result WORDECT_NOT_FOUNDED = new Result("701", "数据库中找不到该单词");

        // 小说模块
        Result BOOK_NOT_FOUNDED = new Result("800", "找不到书籍");
        Result BOOKSHELF_EMPTY = new Result("801", "用户书架为空");
        Result BOOK_ADD_ERROR = new Result("802", "书籍加入书架失败");
        Result BOOK_REMOVE_ERROR = new Result("803", "书籍移出书架失败");
        Result BOOK_LOC_UPDATE_ERROR = new Result("804", "更新书籍最新阅读位置失败");
        Result BOOK_HOT_EMPTY = new Result("805", "热门书籍为空");
        Result CHAPTER_LIST_EMPTY = new Result("806", "书籍章节列表为空");
        Result CHAPTER_EMPTY = new Result("807", "章节内容为空");
        Result PARA_CN_EMPTY = new Result("808", "段落翻译为空");

    }
}
