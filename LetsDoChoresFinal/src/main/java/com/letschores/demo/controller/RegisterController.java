package com.letschores.demo.controller;

import com.letschores.demo.model.User;
import com.letschores.demo.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@Controller
@Validated
public class RegisterController {

    private UserService userService;

    @Autowired
    public RegisterController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model){
        User user=new User();
        model.addAttribute("user",user);
        return "register.html";
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void createUser(@Valid @ModelAttribute ("user") User user){

           try{
               userService.createUser(user);
           }catch (Exception e){
               throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Invalid Data, try again", e);
           }

    }
}
