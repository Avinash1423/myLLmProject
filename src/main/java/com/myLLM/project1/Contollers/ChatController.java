package com.myLLM.project1.Contollers;

import com.myLLM.project1.Model.ChatMessageObject;
import com.myLLM.project1.Model.ChatMessageObjectRepository;
import com.myLLM.project1.Model.ChatSessionObject;
import com.myLLM.project1.Model.ChatSessionObjectRepository;
import com.myLLM.project1.Components.ParseAndRender;
import com.myLLM.project1.Prompt.Explain;
import com.myLLM.project1.Prompt.Greeting;
import com.myLLM.project1.Prompt.SummaryMethod;
import com.myLLM.project1.Tools.WebConnect;
import jakarta.servlet.http.HttpSession;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


import java.io.FileNotFoundException;
import java.util.*;

@Controller
public class ChatController {


    private final ChatClient chatClient;


    List<ChatMessageObject> listOfChatMessages = new ArrayList<>();



    Greeting greeting;
    ParseAndRender parseAndRender;
    SummaryMethod summaryMethod;
    Explain explain;
    WebConnect webConnect;
    ChatMessageObjectRepository chatMessageObjectRepository;
    ChatSessionObjectRepository chatSessionObjectRepository;


    @Autowired
    public ChatController(ChatClient chatClient,Greeting greeting, ParseAndRender parseAndRender, SummaryMethod summaryMethod, Explain explain,WebConnect webConnect, ChatMessageObjectRepository chatMessageObjectRepository,ChatSessionObjectRepository chatSessionObjectRepository) {

        this.chatClient = chatClient;
        this.greeting=greeting;
        this.parseAndRender=parseAndRender;
        this.summaryMethod=summaryMethod;
        this.explain=explain;
        this.webConnect=webConnect;
        this.chatMessageObjectRepository=chatMessageObjectRepository;
        this.chatSessionObjectRepository=chatSessionObjectRepository;

    }


    @GetMapping("/")
    public String showForm(Model model) {

       String greet= greeting.greetingMethod();

        model.addAttribute("greeting", greet);

        return "newChat";
        // return "chat";
    }

    @PostMapping("/ask")
    public String askAi(@RequestParam(required = false) String question, @RequestParam(required = false) String action, Model model, HttpSession session) throws FileNotFoundException {

        Integer chatId= (Integer) session.getAttribute("currentChatId");
        String email= (String) session.getAttribute("loggedInEmail");
        List<ChatSessionObject> listofSessions=chatSessionObjectRepository.findByEmail(email);
        model.addAttribute("listofSessions",listofSessions);



        if (question == null || question.isBlank() || action == null || action.isBlank()) {

            List<ChatMessageObject>listOfChatMessageObjects=chatMessageObjectRepository.findByChatId(chatId);
            model.addAttribute("listOfChatMessages", listOfChatMessageObjects);
            model.addAttribute("listofSessions",listofSessions);


            if(chatId==null) {
                String greet = greeting.greetingMethod();
                model.addAttribute("greeting", greet);
            }
            return "newChat";

        }
        if (action.equals("submit")) {

            String answer = chatClient.prompt().system("You can search the web and answer,if you do not know the answer to the question.").user(question).tools(webConnect).call().content();
            String  ParsedAnswer =parseAndRender.PrMethod(answer);


       /// if chatId is null//creating a new chatSession//

            if(chatId==null){
                 String tile= question.length()>26?question.substring(0,26):question;
                ChatSessionObject chatSessionObject= new ChatSessionObject(email,tile);
                chatSessionObjectRepository.save(chatSessionObject);
                chatId=chatSessionObject.getChatId();
                System.out.println("CHECK The chat Id of the new chatSession: "+ chatId);
                session.setAttribute("currentChatId",chatId);

            }

        /// if chatId is null//creating a new chatSession

//   *         ChatMessageObject chatMessage = new ChatMessageObject(question, ParsedAnswer);
//   *         listOfChatMessages.add(chatMessage);

            ChatMessageObject chatMessage=new ChatMessageObject(chatId,question,ParsedAnswer);
            chatMessageObjectRepository.save(chatMessage);

            List<ChatMessageObject>listOfChatMessageObjects=chatMessageObjectRepository.findByChatId(chatId);
            model.addAttribute("listOfChatMessages", listOfChatMessageObjects);
            model.addAttribute("listofSessions",listofSessions);

            return "newChat";
        }


        if(action.equals("summarize")){

          String summary= summaryMethod.summarize(question);

            if(chatId==null){

                ChatSessionObject chatSessionObject= new ChatSessionObject(email,"Summarize-Request");
                chatSessionObjectRepository.save(chatSessionObject);
                chatId=chatSessionObject.getChatId();
                System.out.println("CHECK The chat Id of the new chatSession: "+ chatId);
                session.setAttribute("currentChatId",chatId);

            }
            ChatMessageObject chatMessage=new ChatMessageObject(chatId,question,summary);
            chatMessageObjectRepository.save(chatMessage);

            List<ChatMessageObject>listOfChatMessageObjects=chatMessageObjectRepository.findByChatId(chatId);
            model.addAttribute("listOfChatMessages", listOfChatMessageObjects);
            model.addAttribute("listofSessions",listofSessions);

            return "newChat";


        }

        if(action.equals("explain")){

        String explanation=explain.explainMethod(question);

            if(chatId==null){

                ChatSessionObject chatSessionObject= new ChatSessionObject(email,"Explanation-Request");
                chatSessionObjectRepository.save(chatSessionObject);
                chatId=chatSessionObject.getChatId();
                System.out.println("CHECK The chat Id of the new chatSession: "+ chatId);
                session.setAttribute("currentChatId",chatId);

            }

            ChatMessageObject chatMessage=new ChatMessageObject(chatId,question,explanation);
            chatMessageObjectRepository.save(chatMessage);

            List<ChatMessageObject>listOfChatMessageObjects=chatMessageObjectRepository.findByChatId(chatId);
            model.addAttribute("listOfChatMessages", listOfChatMessageObjects);
            model.addAttribute("listofSessions",listofSessions);
            return "newChat";



        }

        return"redirect:/newChat";

    }


    }
