package com.myLLM.project1.Contollers;

import com.myLLM.project1.Model.ChatMessageObject;
import com.myLLM.project1.Model.ChatMessageObjectRepository;
import com.myLLM.project1.Model.ChatSessionObject;
import com.myLLM.project1.Model.ChatSessionObjectRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;

import java.util.List;

@Controller
public class NewChatButton {

    public NewChatButton(ChatMessageObjectRepository chatMessageObjectRepository, ChatSessionObjectRepository chatSessionObjectRepository) {
        this.chatMessageObjectRepository = chatMessageObjectRepository;
        this.chatSessionObjectRepository = chatSessionObjectRepository;
    }

    ChatMessageObjectRepository chatMessageObjectRepository;
    ChatSessionObjectRepository chatSessionObjectRepository;


    @GetMapping("/newChatButton")
    public String newChatButton(HttpSession session,Model model){


        session.setAttribute("currentChatId",null);
        Integer chatId= (Integer) session.getAttribute("currentChatId");
        String email= (String) session.getAttribute("loggedInEmail");
        List<ChatMessageObject> listOfChatMessages=  chatMessageObjectRepository.findByChatId(chatId);
        model.addAttribute("listOfChatMessages",listOfChatMessages);
        List<ChatSessionObject> listofSessions=chatSessionObjectRepository.findByEmail(email);
        model.addAttribute("listofSessions",listofSessions);
        session.setAttribute("currentChatId",chatId);



        return "newChat";


    }
}
