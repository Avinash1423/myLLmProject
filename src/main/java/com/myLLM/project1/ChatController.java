package com.myLLM.project1;

import ch.qos.logback.core.joran.util.beans.BeanDescriptionFactory;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.commonmark.Extension;
import org.commonmark.ext.gfm.tables.TablesExtension;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;




import javax.swing.text.html.parser.DTD;
import java.util.*;

@Controller
public class ChatController {


    private final ChatClient chatClient;
    List<ChatMessageObject> listOfChatMessages= new ArrayList<>();

    @Autowired
    public ChatController(ChatClient chatClient){

        this.chatClient=chatClient;
    }



    @GetMapping("/")
    public String showForm(){

        return "newChat";

        // return "chat";
    }

    @PostMapping("/ask")
    public String askAi(@RequestParam(required = true) String question, Model model){

        String answer=chatClient.prompt().user(question).call().content();

        // Parse and render
        Parser parser= Parser.builder().extensions(List.of(TablesExtension.create())).build();
        Node document=parser.parse(answer);
        HtmlRenderer htmlRenderer=HtmlRenderer.builder().extensions((List.of(TablesExtension.create()))).build();
        String parseredAnswer=htmlRenderer.render(document);


        // ensure Security
        String safeHtml= Jsoup.clean(parseredAnswer,Safelist.relaxed());



        ChatMessageObject chatMessage=new ChatMessageObject(question,safeHtml);

        listOfChatMessages.add(chatMessage);
        model.addAttribute("listOfChatMessages",listOfChatMessages);

        return "newChat";

        //        model.addAttribute("questionFromPost",question);
//        model.addAttribute("answerFromPost",answer);


        //  return "chat";


    }



}
