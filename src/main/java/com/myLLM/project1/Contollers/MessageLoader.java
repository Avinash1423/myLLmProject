package com.myLLM.project1.Contollers;

import com.myLLM.project1.Model.ChatMessageObject;
import com.myLLM.project1.Model.ChatMessageObjectRepository;
import com.myLLM.project1.Model.ChatSessionObject;
import com.myLLM.project1.Model.ChatSessionObjectRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class MessageLoader {

    ChatSessionObjectRepository chatSessionObjectRepository;

    public MessageLoader(ChatSessionObjectRepository chatSessionObjectRepository, ChatMessageObjectRepository chatMessageObjectRepository) {
        this.chatSessionObjectRepository = chatSessionObjectRepository;
        this.chatMessageObjectRepository = chatMessageObjectRepository;
    }

    ChatMessageObjectRepository chatMessageObjectRepository;


    @PostMapping("/loadMessages")
    public String loadMessages(@RequestParam(required = false) Integer chatId, Model model, HttpSession session){

        String email= (String) session.getAttribute("loggedInEmail");
     List<ChatMessageObject> listOfChatMessages=  chatMessageObjectRepository.findByChatId(chatId);
     model.addAttribute("listOfChatMessages",listOfChatMessages);
     List<ChatSessionObject> listofSessions=chatSessionObjectRepository.findByEmail(email);
     model.addAttribute("listofSessions",listofSessions);
     session.setAttribute("currentChatId",chatId);



     return "newChat";

    }
}
