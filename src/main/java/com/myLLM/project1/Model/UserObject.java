package com.myLLM.project1.Model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="llmuser")
public class UserObject {

    public UserObject() {

    }

    @Id
    @Column(name="email" ,nullable = false)
    String email;

    @Column(name="passwordkey" ,nullable = false)
    String Password;

    @OneToMany(mappedBy = "user")
    List<ChatSessionObject> listofChatSession=new ArrayList<>();

    public UserObject(String email, String password) {
        this.email = email;
        Password = password;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }




}
