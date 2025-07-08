package com.myLLM.project1.Model;


import jakarta.persistence.*;
import org.neo4j.driver.Config;

import javax.annotation.Nullable;


@Entity
@Table(name="chatessage")
public class ChatMessageObject {

    public ChatMessageObject() {

    }

    public ChatMessageObject( Integer chatId,String question,String answer){

        this.chatId=chatId;
        this.question=question;
        this.answer=answer;

    }


    public ChatMessageObject( String question,String answer){


        this.question=question;
        this.answer=answer;

    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="messageid" , nullable=false)
    Integer messageId;

    public Integer getChatId() {
        return chatId;
    }

    @Column(name="chatid" ,nullable=false)
    Integer chatId;




    @Column(name="question",nullable = false,length=1000)
    String question;


    @Column(name="answer",nullable = false,length=2000)
    String answer;

     @ManyToOne
     @JoinColumn(name="chatid",insertable = false,updatable = false,foreignKey =@ForeignKey(name="fk_Cm_Csh"))
    ChatSessionObject chatSessionObject;

    public Integer getMessageId() {return messageId;}

    public String getQuestion() {
        return question;
    }


    public String getAnswer() {
        return answer;
    }




}

