package com.tikeii.nghienchat.model;

import com.google.firebase.Timestamp;

public class UserModel {
    String name;
    String phone;
    Timestamp createtime;
    String ID;
    String token;

    public UserModel() {
    }

    public UserModel(String name, String phone, Timestamp createtime, String ID) {
        this.name = name;
        this.phone = phone;
        this.createtime = createtime;
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Timestamp getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Timestamp createtime) {
        this.createtime = createtime;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
