package com.ahuiali.word.pojo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by shkstart on 2019/10/20
 */
@Data
public class Learner implements Serializable {

    private Integer id;

    //激活码
    private String activecode;

    //密码
    private String password;

    //昵称，唯一
    private String nickname;

    //邮箱，唯一
    private String email;

    //状态 0未激活，1 正常， 2 封禁中
    private Integer status;

    //创建日期
    private Date created;

    //修改日期
    private Date modified;


}
