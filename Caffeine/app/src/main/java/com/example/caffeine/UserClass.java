package com.example.caffeine;

public class UserClass {

    public UserClass(String name, String email, String password,
                     String phone, String id,String imgUrl) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.id = id;
        this.imgUrl = imgUrl;
    }

    public UserClass() {
    }

    private String id;
    private String name;
    private String email;
    private String password;
    private String phone;
    private String imgUrl;

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }



}
