package com.myLLM.project1;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import org.springframework.http.HttpHeaders;
import org.springframework.ui.Model;


@Controller
public class AudioController {

    ChatController chatController;
    @Autowired
    public AudioController(  ChatController chatController){

        this.chatController=chatController;
    }

    @PostMapping("/transcribe")
    public String transcribe(@RequestParam("file")MultipartFile file, Model model) throws IOException {

        //form header
        HttpHeaders headers = new HttpHeaders();

        headers.setBearerAuth("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");
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

       ChatMessageObject chatMessageObject =new ChatMessageObject(question,transcript);

       chatController.listOfChatMessages.add(chatMessageObject);

        model.addAttribute("listOfChatMessages", chatController.listOfChatMessages);
        return "newChat";

    }
}