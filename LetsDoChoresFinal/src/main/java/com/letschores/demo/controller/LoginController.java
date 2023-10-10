package com.letschores.demo.controller;

import com.letschores.demo.model.User;
import com.letschores.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;

@Controller
public class LoginController {



    @GetMapping("/login")
    public String loginForm(Model model){
        return "login";
    }



}
