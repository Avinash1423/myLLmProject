package com.myLLM.project1;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class Greeting {

    ChatClient chatClient;
    @Autowired
    public Greeting(ChatClient chatClient) {

        this.chatClient=chatClient;
    }



    @Value("classpath:/Prompts/greetingTemp.st")
    Resource greetingTemp;




    public String greetingMethod(){


        SystemPromptTemplate greetingSysPromtTemplate=new SystemPromptTemplate(greetingTemp);

        SystemMessage greetingSystemMessage= (SystemMessage) greetingSysPromtTemplate.createMessage();

        String greetingPrompt=greetingSystemMessage.getText();

        return chatClient.prompt().system(sys->sys.text(greetingPrompt)).call().content();


    }


}
