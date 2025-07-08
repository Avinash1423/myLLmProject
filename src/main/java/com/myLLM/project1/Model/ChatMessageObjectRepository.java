package com.myLLM.project1.Model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;

import javax.validation.constraints.Pattern;
import java.util.List;

public interface ChatMessageObjectRepository extends JpaRepository<ChatMessageObject,Integer> {


    List<ChatMessageObject>findByChatId(Integer chatId);


}
