package com.myLLM.project1.Contollers;

import com.myLLM.project1.Model.ChatSessionObject;
import com.myLLM.project1.Model.ChatSessionObjectRepository;
import com.myLLM.project1.Model.UserObejectRepository;
import com.myLLM.project1.Model.UserObject;
import com.myLLM.project1.Prompt.Greeting;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
//bcrytpt not used for this project as it is outofscope;
@Controller
public class LoginController {

 Greeting greeting;
 ChatSessionObjectRepository chatSessionObjectRepository;
 UserObejectRepository userObejectRepository;

 @Autowired
    public LoginController(Greeting greeting,ChatSessionObjectRepository chatSessionObjectRepository, UserObejectRepository userObejectRepository) {
        this.greeting = greeting;
        this.chatSessionObjectRepository=chatSessionObjectRepository;
        this.userObejectRepository=userObejectRepository;
    }


    @GetMapping("/loginPage")
        public String LoginPage(){

        return "userLogin";

    }

    @PostMapping("/validateLogin")
    public  String LoginMethod(@RequestParam(required = false) String email, @RequestParam(required = false) String password,Model model,HttpSession session){


              if(email==null||email.isBlank()||password==null||password.isBlank()){

                  model.addAttribute("emptyFieldError","Please fill all fields");
                  return "userLogin";
              }

        boolean exists=userObejectRepository.findById(email).isPresent();
        UserObject loggedInUser;
        String passwordInDB;

     if(!exists){

         model.addAttribute("accountDoesNotExistError","The account does not exist");
         return "userLogin";

     }


         loggedInUser=userObejectRepository.findById(email).get();
         passwordInDB=loggedInUser.getPassword();

         if(!password.equals(passwordInDB)){

             model.addAttribute("passwordIncorrectError","The password is not correct");
             return "userLogin";


         }




        // once string and email are verified

        session.setAttribute("loggedInEmail",email);
        session.setAttribute("currentChatId",null);

        List<ChatSessionObject> listofSessions=chatSessionObjectRepository.findByEmail(email);


        String greet= greeting.greetingMethod();

        model.addAttribute("listofSessions",listofSessions);
        model.addAttribute("greeting", greet);

         return "newChat";


    }
}
