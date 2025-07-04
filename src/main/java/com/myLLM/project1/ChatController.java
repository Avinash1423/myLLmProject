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


    @Value("classpath:/Prompts/summaryTemp.st")
    Resource summaryTemp;

    @Value("classpath:/Prompts/explanationTemp.st")
    Resource explanationTemp;


    private final ChatClient chatClient;
    List<ChatMessageObject> listOfChatMessages = new ArrayList<>();

    @Autowired
    public ChatController(ChatClient chatClient) {

        this.chatClient = chatClient;
    }


    @GetMapping("/")
    public String showForm(Model model) {



        String greetingTemp = "Greet the user like exactly  this: Hello , how can i help you?";

        String greeting = chatClient.prompt().system(greetingTemp).call().content();

        model.addAttribute("greeting", greeting);

        return "newChat";

        // return "chat";
    }

    @PostMapping("/ask")
    public String askAi(@RequestParam(required = false) String question, @RequestParam(required = false) String action, Model model) throws FileNotFoundException {


        if (question == null || question.isBlank() || action == null || action.isBlank()) {

            String greetingTemp = "Greet the user like exactly  this: Hello , how can i help you?";

            String greeting = chatClient.prompt().system(greetingTemp).call().content();

            model.addAttribute("greeting", greeting);

            return "newChat";

        }
        if (action.equals("submit")) {

            String answer = chatClient.prompt().user(question).call().content();

            // Parse and render
            Parser parser = Parser.builder().extensions(List.of(TablesExtension.create())).build();
            Node document = parser.parse(answer);
            HtmlRenderer htmlRenderer = HtmlRenderer.builder().extensions((List.of(TablesExtension.create()))).build();
            String parseredAnswer = htmlRenderer.render(document);


            // ensure Security
            String safeHtml = Jsoup.clean(parseredAnswer, Safelist.relaxed());


            ChatMessageObject chatMessage = new ChatMessageObject(question, safeHtml);

            listOfChatMessages.add(chatMessage);
            model.addAttribute("listOfChatMessages", listOfChatMessages);

            return "newChat";
        }


        if(action.equals("summarize")){
            //summary logic**********
            System.out.println("Inside summarize");
            SystemPromptTemplate systemPromptTemplate = new SystemPromptTemplate(summaryTemp);

            System.out.println("summary temp is accessed");
          SystemMessage summaryPrompt= (SystemMessage) systemPromptTemplate.createMessage(Map.of("user-input",question));

            System.out.println("mapped user input to question");
            String promptText=summaryPrompt.getText();

            System.out.println(promptText);

            String summary=chatClient.prompt().system(sys->sys.text(promptText)).call().content();

            //summary logic**********



            // Parse and render
            System.out.println("summary parsing" );
             Parser parser = Parser.builder().extensions(List.of(TablesExtension.create())).build();
            Node Summarydocument = parser.parse(summary);
            HtmlRenderer htmlRenderer = HtmlRenderer.builder().extensions((List.of(TablesExtension.create()))).build();
            String parseredSummary = htmlRenderer.render(Summarydocument);


            // ensure Security
            String SummarysafeHtml = Jsoup.clean(parseredSummary, Safelist.relaxed());


            ChatMessageObject chatMessage = new ChatMessageObject(question, SummarysafeHtml);

            listOfChatMessages.add(chatMessage);
            model.addAttribute("listOfChatMessages", listOfChatMessages);

            return "newChat";


        }

        if(action.equals("explain")){

         //explanation logic***
            SystemPromptTemplate  systemPromptTemplate= new SystemPromptTemplate(explanationTemp);

            SystemMessage explanationMessage=(SystemMessage)systemPromptTemplate.createMessage(Map.of("user-input",question));

            String explanationPrompt=explanationMessage.getText();

            String explanation=chatClient.prompt().system(sys->sys.text(explanationPrompt)).call().content();

            //explanation logic***

            //parse and render

            Parser parser= Parser.builder().build();
            Node explanationDocx= parser.parse(explanation);
            HtmlRenderer renderer=HtmlRenderer.builder().extensions(List.of(TablesExtension.create())).build();
            String parsedExplanation= renderer.render(explanationDocx);

            //ensure security
            String safeExplanation=Jsoup.clean(parsedExplanation,Safelist.relaxed());

            ChatMessageObject chatMessageObject= new ChatMessageObject(question,safeExplanation);


            listOfChatMessages.add(chatMessageObject);
            model.addAttribute("listOfChatMessages", listOfChatMessages);

            return "newChat";




        }

        return"redirect:/newChat";

    }


    }
