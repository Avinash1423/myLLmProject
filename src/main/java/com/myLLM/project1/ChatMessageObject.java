package com.myLLM.project1;

public class ChatMessageObject {

    public String getQuestion() {
        return question;
    }

    String question;

    public String getAnswer() {
        return answer;
    }

    String answer;

    public ChatMessageObject(String question,String answer){

        this.question=question;
        this.answer=answer;

    }



}

