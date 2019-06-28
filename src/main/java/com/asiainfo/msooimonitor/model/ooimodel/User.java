package com.asiainfo.msooimonitor.model.ooimodel;

/**
 * @Author H
 * @Date 2019/2/21 15:57
 * @Desc
 **/
public class User {

    private String userName;

    private String password;

    public User(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
