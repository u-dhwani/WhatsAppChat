package com.example.whatsappchat.Models;

import android.text.Editable;

public class Users {
    String phone;
    String profilepic,userName,mail,password,userId,lastMessage,status;

    public Users(String profilepic, String userName, String mail, String password, String userId, String lastMessage, String status, String phone) {
        this.profilepic = profilepic;
        this.userName = userName;
        this.mail = mail;
        this.password = password;
        this.userId = userId;
        this.lastMessage = lastMessage;
        this.status=status;
        this.phone=phone;
    }
    public  Users()
    {

    }
    //SignUp Constructor
    public Users( String userName, String mail,String phone,String password) {
        this.userName = userName;
        this.mail = mail;
        this.phone=phone;
        this.password = password;

    }


    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getProfilepic() {
        return profilepic;
    }

    public void setProfilepic(String profilepic) {
        this.profilepic = profilepic;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }



    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }
}
