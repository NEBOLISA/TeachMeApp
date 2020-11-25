package com.hfad.teach.Models;

public class Users {
    private String name;
    private String user_id;
    private String email;
    private String user_type;



    public Users(String name, String user_id, String email,String user_type) {
        this.name = name;
        this.user_id = user_id;
        this.email = email;
        this.user_type = user_type;
    }
    public Users() {
    }
    public Users(String name) {
        this.name = name;
    }

    public String getEmail(){
        return email;
    }
public void setEmail(String email){
        this.email = email;

}
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }
    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

}
