package com.apps.philipps.app.activities;

import java.io.Serializable;

public class UserData implements Serializable {
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getExp() {
        return exp;
    }

    public void setExp(String exp) {
        this.exp = exp;
    }

    public String getEmailOfDoctor() {
        return emailOfDoctor;
    }

    public void setEmailOfDoctor(String emailOfDoctor) {
        this.emailOfDoctor = emailOfDoctor;
    }

    private String name;
    private int age;
    private String email;
    private String exp;
    private String emailOfDoctor;


}
