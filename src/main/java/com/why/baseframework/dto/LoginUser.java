package com.why.baseframework.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.why.baseframework.constants.IntConstants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author Y
 * @Description: 商城登录用户表
 * @Title: LoginUser
 * @ProjectName base_framework
 * @Date 2021/4/16 15:20
 * @Company WHY-Group
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginUser implements Serializable {

    /**
     * 序列化ID
     */
    private static final long serialVersionUID = 8896484968461011677L;

    /**
     * id
     */
    private String id;
    /**
     * 身份证号
     */
    private String idCard;

    /**
     * 账号
     */
    private String account;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 姓名
     */
    private String name;

    /**
     * 密码：不给前端页面返回此数据
     */
    @JsonIgnore
    private String password;

    /**
     * 微信号
     */
    private String wxOpenId;

    /**
     * 角色
     */
    private String roles;

    /**
     * 登录认证
     */
    private String token;

    /**
     * 头像的url
     */
    private String headImgUrl;
    /**
     * 请求来源
     */
    private String requestSource;
    /**
     * 认证与否
     */
    private Boolean qualificationFlag;
    /**
     * 扩展属性
     */
    private Object extraInfo;


    /**
     * 获取ID
     *
     * @return String the id
     * @author W
     * @date 2021-05-29
     */
    public String getRedisId() {
        return this.id + (char) IntConstants.INT_45 + this.requestSource;
    }
}


