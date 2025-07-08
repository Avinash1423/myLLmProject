package com.myLLM.project1.Contollers;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static org.apache.commons.lang3.exception.ExceptionUtils.getMessage;

@ControllerAdvice
public class GlobalErrorHandler {


    public GlobalErrorHandler() {
    }

    @ExceptionHandler(Exception.class)
    public String customerErrorHandler(Exception e, Model model){
     String error= e.getMessage();
     model.addAttribute("errorMessage",error);

        return"errorPage";

    }


}
