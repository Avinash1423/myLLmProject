package com.myLLM.project1;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.memory.repository.neo4j.Neo4jChatMemoryRepository;
import org.springframework.ai.vectorstore.pinecone.PineconeVectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class AiConfiguration {

    Neo4jChatMemoryRepository chatMemoryRepository;
    PineconeVectorStore vectorStore;




    @Autowired
    public  AiConfiguration(  Neo4jChatMemoryRepository chatMemoryRepository,PineconeVectorStore vectorStore){
        this.chatMemoryRepository=chatMemoryRepository;
        this.vectorStore=vectorStore;

    }

    @Bean
    public ChatMemory chatMemory(){
        return MessageWindowChatMemory.builder().chatMemoryRepository(chatMemoryRepository).maxMessages(10).build();

    }

    @Bean
    public ChatClient chatClient(ChatClient.Builder builder, ChatMemory chatMemory, PineconeVectorStore vectorStore){

        return builder.defaultAdvisors( MessageChatMemoryAdvisor.builder(chatMemory).build(), QuestionAnswerAdvisor.builder(vectorStore).build()).build();

    }


}