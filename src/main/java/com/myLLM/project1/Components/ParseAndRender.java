package com.myLLM.project1.Components;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.commonmark.ext.gfm.tables.TablesExtension;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ParseAndRender {


    ChatClient chatClient;

    @Autowired
    public ParseAndRender(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    public String PrMethod(String answer) {


        Parser parser = Parser.builder().build();
        HtmlRenderer htmlRenderer = HtmlRenderer.builder().extensions(List.of(TablesExtension.create())).build();

      //  String answer = chatClient.prompt().user(question).call().content();


        Node document = parser.parse(answer);

        String parsedAnswer = htmlRenderer.render(document);

      return  Jsoup.clean(parsedAnswer, Safelist.relaxed());


       // return new ChatMessageObject(question, safeHtml);


    }
}
