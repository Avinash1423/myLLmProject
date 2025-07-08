package com.myLLM.project1.Tools;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriUtils;


import java.nio.charset.StandardCharsets;

@Component
public class WebConnect {


    @Tool(description = "Search the web for teh query and return the top Results")
    public String search(String query) throws JsonProcessingException {


       System.out.println("query sent to API");//printed
        return callSearchApi(query);


    }

    public String callSearchApi(String query) throws JsonProcessingException {


        try {
            String apiKey =System.getenv("ScraperAPIKey");
            String encoded = UriUtils.encode(query, StandardCharsets.UTF_8);
            String url = "https://api.scraperapi.com/structured/google/search" +
                    "?api_key=" + apiKey +
                    "&query=" + encoded;

            RestTemplate restTemplate = new RestTemplate();
            String json = restTemplate.getForObject(url, String.class);

            System.out.println("CHECK 1 "+json);

            return resultFilter( json); // printed
        } catch (Exception e) {
            e.printStackTrace();

            return "Search error: " + e.getMessage();
        }


    }

    public String resultFilter(String json) throws JsonProcessingException {

        System.out.println("CHECK 2  Inside resultFilter");//prints

        ObjectMapper mapper=new ObjectMapper();
        JsonNode root=mapper.readTree(json);

        JsonNode results=root.path("organic_results");
        System.out.println("CHECK 3  "+results);
        if(results.isArray() && results.size()>0){

            JsonNode first=results.get(0);

            String title=first.path("title").asText("");

            String snippet=first.path("snippet").asText("");

            System.out.println( "CHECK 4 "+title);
            System.out.println("CHECK 5 "+snippet);


            return  title+": \n "+snippet;


        }

            return"No results found";

    }
}
