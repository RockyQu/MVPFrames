package com.frame.mvp.entity;

import com.frame.mvp.entity.base.BaseEntity;
import com.google.gson.annotations.SerializedName;

/**
 * 用户信息
 */
public class User extends BaseEntity.Bean {

    //ID
    @SerializedName("userid")
    private String userId;
    //账号
    private String account;
    //密码
    private String password;
    //名字
    @SerializedName("realname")
    private String name;
    //性别
    @SerializedName("gender")
    private String gender;
    //昵称
    @SerializedName("nickname")
    private String nickname;
    //移动电话
    @SerializedName("mobile")
    private String mobilePhone;
    //生日
    @SerializedName("birth")
    private String birth;
    //头像
    @SerializedName("avatar")
    private String avatarUrl;
    //地区、地址
    @SerializedName("area")
    private String area;
    //积分
    @SerializedName("point")
    private String point;
    //签名
    @SerializedName("signature")
    private String signature;

    //Token
    @SerializedName("access_token")
    private String token;

    public User() {

    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}