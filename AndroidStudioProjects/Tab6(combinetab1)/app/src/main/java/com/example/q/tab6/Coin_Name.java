package com.example.q.tab6;

public class Coin_Name {
    private String name;
    private String birthday;
    private String sex;

    public Coin_Name(String name, String birthday, String sex) {
        this.birthday = birthday;
        this.name = name;
        this.sex = sex;
    }
    public String getBirthday() {
        return birthday;
    }

    public void setBirthday() {
        this.birthday = birthday;
    }
    public String getName() {
        return name;
    }

    public void setName() {
        this.name = name;
    }
    public String getSex() {
        return sex;
    }

    public void setSex() {
        this.sex = sex;
    }
}
