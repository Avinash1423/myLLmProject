package com.myLLM.project1;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class SummaryMethod {


    @Value("classpath:/Prompts/summaryTemp.st")
    Resource summaryTemp;

    ChatClient chatClient;
    ParseAndRender parseAndRender;

    public SummaryMethod(ChatClient chatClient, ParseAndRender parseAndRender) {
        this.chatClient = chatClient;
        this.parseAndRender=parseAndRender;
    }


    public String summarize( String question){


        System.out.println("Inside summarize");
        SystemPromptTemplate systemPromptTemplate = new SystemPromptTemplate(summaryTemp);

        System.out.println("summary temp is accessed");
        SystemMessage summaryPrompt= (SystemMessage) systemPromptTemplate.createMessage(Map.of("user-input",question));

        System.out.println("mapped user input to question");
        String promptText=summaryPrompt.getText();

        System.out.println(promptText);

       String summary= chatClient.prompt().system(sys->sys.text(promptText)).call().content();

       return parseAndRender.PrMethod(summary);
    }

}
