package com.letschores.demo.controller;

import com.letschores.demo.model.Chore;
import com.letschores.demo.model.Review;
import com.letschores.demo.model.User;
import com.letschores.demo.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String playerHome(Model model){
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        String username = authentication.getName();
        User user=userService.findUserByUserName(username);
        model.addAttribute("user",user);

        return "index";
    }


    @GetMapping("/forwarder")
    public String redirectToCreatePlayerForm(Principal principal){
        User user=userService.findUserByUserName(principal.getName());
        return "redirect:/player/createPlayer?userId=" + user.getId();
    }

    @GetMapping("/userProfile")
    public String userProfile(Principal principal,Model model){
        User user=userService.findUserByUserName(principal.getName());
        model.addAttribute("user",user);
        return "display-user";

    }

    @GetMapping("/viewAllChores")
    public String viewAllChores(Principal principal,Model model){
        User user=userService.findUserByUserName(principal.getName());
        List<Chore> choreList=new ArrayList<>();
        choreList.addAll(user.getPlayer1().getChores());
        choreList.addAll(user.getPlayer2().getChores());

        model.addAttribute("chores",choreList);
        return "display-all-chores";
    }

    @GetMapping("/findUserReviews")
    public String findUserReviews(Principal principal,Model model){
        User user=userService.findUserByUserName(principal.getName());
        List<Review> allReviews=new ArrayList<>();
        List<Chore> choreList=new ArrayList<>();
        choreList.addAll(user.getPlayer1().getChores());
        choreList.addAll(user.getPlayer2().getChores());
        for (Chore chore:choreList){
            for (Review review:chore.getReviews()){
                allReviews.add(review);
            }
        }
        model.addAttribute("reviews",allReviews);

        return "display-reviews";
    }

}
