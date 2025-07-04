package com.myLLM.project1;

import ch.qos.logback.core.joran.util.beans.BeanDescriptionFactory;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.commonmark.Extension;
import org.commonmark.ext.gfm.tables.TablesExtension;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;




import javax.swing.text.html.parser.DTD;
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

    @Autowired
    public ChatController(ChatClient chatClient,Greeting greeting, ParseAndRender parseAndRender, SummaryMethod summaryMethod, Explain explain) {

        this.chatClient = chatClient;
        this.greeting=greeting;
        this.parseAndRender=parseAndRender;
        this.summaryMethod=summaryMethod;
        this.explain=explain;
    }


    @GetMapping("/")
    public String showForm(Model model) {

       String greet= greeting.greetingMethod();


        model.addAttribute("greeting", greet);

        return "newChat";
        // return "chat";
    }

    @PostMapping("/ask")
    public String askAi(@RequestParam(required = false) String question, @RequestParam(required = false) String action, Model model) throws FileNotFoundException {


        if (question == null || question.isBlank() || action == null || action.isBlank()) {

            String greet= greeting.greetingMethod();

          model.addAttribute("greeting", greet);

            return "newChat";

        }
        if (action.equals("submit")) {

            String answer = chatClient.prompt().user(question).call().content();

            String  ParsedAnswer =parseAndRender.PrMethod(answer);

           ChatMessageObject chatMessage= new ChatMessageObject(question,ParsedAnswer);

            listOfChatMessages.add(chatMessage);

            model.addAttribute("listOfChatMessages", listOfChatMessages);

            return "newChat";
        }


        if(action.equals("summarize")){

          String summary= summaryMethod.summarize(question);

            ChatMessageObject chatMessage=new ChatMessageObject(question,summary);
            listOfChatMessages.add(chatMessage);
            model.addAttribute("listOfChatMessages", listOfChatMessages);

            return "newChat";


        }

        if(action.equals("explain")){

        String explanation=explain.explainMethod(question);

            ChatMessageObject chatMessageObject= new ChatMessageObject(question,explanation);

            listOfChatMessages.add(chatMessageObject);
            model.addAttribute("listOfChatMessages", listOfChatMessages);

            return "newChat";




        }

        return"redirect:/newChat";

    }


    }
