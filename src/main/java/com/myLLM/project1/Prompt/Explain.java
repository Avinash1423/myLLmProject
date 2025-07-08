package com.myLLM.project1.Prompt;

import com.myLLM.project1.Components.ParseAndRender;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class Explain {

    @Value("classpath:/Prompts/explanationTemp.st")
    Resource explanationTemp;

    ChatClient chatClient;
    ParseAndRender parseAndRender;

    public Explain(ChatClient chatClient,  ParseAndRender parseAndRender) {
        this.chatClient = chatClient;
        this.parseAndRender=parseAndRender;
    }


    public String explainMethod(String question){

        SystemPromptTemplate systemPromptTemplate= new SystemPromptTemplate(explanationTemp);

        SystemMessage explanationMessage=(SystemMessage)systemPromptTemplate.createMessage(Map.of("user-input",question));

        String explanationPrompt=explanationMessage.getText();

        String explanation=chatClient.prompt().system(sys->sys.text(explanationPrompt)).call().content();

       return parseAndRender.PrMethod(explanation);

    }

}
