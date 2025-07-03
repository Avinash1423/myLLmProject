package com.myLLM.project1;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.ai.transformer.splitter.TextSplitter;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;

import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;


import java.util.List;

@Component
public class Embedding implements CommandLineRunner {

    private static final  Logger log= LoggerFactory.getLogger(Embedding.class);
    VectorStore vectorStore;


    @Value("classpath:/docs/FULLTEXT01.pdf")
    Resource resource;

    List<Document> docs = List.of(new Document("Test input text for embedding"));

    @Autowired
    public Embedding( VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    @Override
    public void run(String... args) throws Exception {

        try {
            var pdfReader = new TikaDocumentReader(resource); //give pdfreader the docx to split to paragraphs
            System.out.println("give pdfreader the docx to split to paragraphs");
            TextSplitter textSplitter = new TokenTextSplitter();  //get text splitter
            System.out.println("called test splitter");
            vectorStore.accept(textSplitter.apply(pdfReader.get()));  // split the pdf and give vector store
            System.out.println("passed split docs to vector db");
//            vectorStore.accept(docs);
        } catch (Exception e)
        {
            e.printStackTrace();

        }

    }
}


