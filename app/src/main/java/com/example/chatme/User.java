package com.example.chatme;

import java.io.Serializable;

public class User implements Serializable {

    String name,Uid;

    public User(){

    }

    public User(String name, String uid) {
        this.name = name;
        this.Uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getUid() {
        return this.Uid;
    }

    public void setUid(String uid) {
        this.Uid = uid;
    }
}
