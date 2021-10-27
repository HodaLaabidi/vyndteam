package com.vyndsolutions.vyndteam.models;

import java.io.Serializable;

/**
 * Created by Hoda on 09/02/2018.
 */
public class Admin implements Serializable {



    private String mail ;

    private String password = null ;

   private String token ;

    public String getEmail() {
        return mail;
    }

    public Admin(String mail, String token) {
        this.mail = mail;
        this.token = token;
    }

    public Admin(String mail){
        this.mail = mail ;
    }

    public void setEmail(String email) {
        this.mail = mail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

     public String getToken()
     {
        return token ;
    }

     public void setToken(String token) {
       this.token = token;
    }
}



