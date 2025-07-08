package com.myLLM.project1.Model;

import java.util.List;
import jakarta.persistence.*;

import java.util.ArrayList;

@Entity
@Table(name="chatsession")
public class ChatSessionObject {

    public ChatSessionObject() {

    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="chatid" ,nullable = false)
    Integer chatId;

    @Column(name="title" ,nullable = false)
    String Title;

    @Column(name="email" ,nullable = false)
    String email;

    @ManyToOne
    @JoinColumn(name="email",insertable = false,updatable = false,foreignKey=@ForeignKey(name="fk_u_chs"))
    UserObject user;

    @OneToMany(mappedBy ="chatSessionObject")
    List<ChatMessageObject> listOfChatMessages=new ArrayList<>();

    public ChatSessionObject(String email, String title) {
        this.email = email;
        Title = title;
    }


    public Integer getChatId() {
        return chatId;
    }

    public void setChatId(Integer chatId) {
        this.chatId = chatId;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }




}
