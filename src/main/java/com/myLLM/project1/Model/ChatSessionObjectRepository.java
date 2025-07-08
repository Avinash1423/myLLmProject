package com.myLLM.project1.Model;

import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatSessionObjectRepository extends JpaRepository<ChatSessionObject,Integer> {

    List<ChatSessionObject>findByEmail(String email);



}
