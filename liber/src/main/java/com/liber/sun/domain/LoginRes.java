package com.liber.sun.domain;

/**
 * Created by SongJie on 2019/3/9 18:26
 */
public class LoginRes {
    private User user;
    private String token;

    public LoginRes() {
    }

    public LoginRes(User user, String token) {
        this.user = user;
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "LoginRes{" +
                "user=" + user +
                ", token='" + token + '\'' +
                '}';
    }
}
