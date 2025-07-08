package com.myLLM.project1.Tools;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.myLLM.project1.Contollers.ChatController;
import com.myLLM.project1.Model.ChatMessageObject;
import com.myLLM.project1.Model.ChatMessageObjectRepository;
import com.myLLM.project1.Model.ChatSessionObject;
import com.myLLM.project1.Model.ChatSessionObjectRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.ui.Model;


@Controller
public class AudioController {


    ChatSessionObjectRepository chatSessionObjectRepository;
    ChatMessageObjectRepository chatMessageObjectRepository;
    ChatController chatController;
    @Autowired
    public AudioController(  ChatController chatController,ChatSessionObjectRepository chatSessionObjectRepository, ChatMessageObjectRepository chatMessageObjectRepository){

        this.chatSessionObjectRepository=chatSessionObjectRepository;
        this.chatMessageObjectRepository= chatMessageObjectRepository;
        this.chatController=chatController;
    }

    @PostMapping("/transcribe")
    public String transcribe(@RequestParam("file")MultipartFile file, Model model, HttpSession session) throws IOException {

        Integer chatId= (Integer) session.getAttribute("currentChatId");
        String email= (String) session.getAttribute("loggedInEmail");
        List<ChatSessionObject> listofSessions=chatSessionObjectRepository.findByEmail(email);
        model.addAttribute("listofSessions",listofSessions);



  ///transcript starts

        //form header
        HttpHeaders headers = new HttpHeaders();

        headers.setBearerAuth(System.getenv("OpenAiKey"));
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        // form body

        MultiValueMap<String,Object> body= new LinkedMultiValueMap<>();
        body.add("file", new ByteArrayResource( file.getBytes()){

            @Override
            public String getFilename() {
                return file.getOriginalFilename();
            }
        });

        body.add("model","whisper-1");

        HttpEntity<MultiValueMap<String,Object>>request=new HttpEntity<>(body,headers);

        RestTemplate restTemplate=new RestTemplate();

        ResponseEntity<String> response=restTemplate.postForEntity(
                "https://api.openai.com/v1/audio/transcriptions",
                request,
                String.class
        );


        ObjectMapper mapper = new ObjectMapper();
        JsonNode root=mapper.readTree(response.getBody());
        String  transcript=root.get("text").asText();

       String question="Request to transcribe audio file "+ file.getOriginalFilename()+". Transcription:";
/// transcript done

/// if chatId is null//creating a new chatSession//

        if(chatId==null){

            ChatSessionObject chatSessionObject= new ChatSessionObject(email,"Transcribe Request");
            chatSessionObjectRepository.save(chatSessionObject);
            chatId=chatSessionObject.getChatId();
            System.out.println("CHECK The chat Id of the new chatSession: "+ chatId);
            session.setAttribute("currentChatId",chatId);

        }

        /// if chatId is null//creating a new chatSession//

        ChatMessageObject chatMessage=new ChatMessageObject(chatId,question,transcript);
        chatMessageObjectRepository.save(chatMessage);

        List<ChatMessageObject>listOfChatMessageObjects=chatMessageObjectRepository.findByChatId(chatId);
        model.addAttribute("listOfChatMessages", listOfChatMessageObjects);
        model.addAttribute("listofSessions",listofSessions);
        return "newChat";

    }
}