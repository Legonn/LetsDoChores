package com.letschores.demo.controller;

import com.letschores.demo.model.Activity;
import com.letschores.demo.model.Player;
import com.letschores.demo.model.User;
import com.letschores.demo.service.ActivityService;
import com.letschores.demo.service.PlayerService;
import com.letschores.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/activity")
public class ActivityController {

    private ActivityService activityService;
    private PlayerService playerService;

    @Autowired
    public ActivityController(ActivityService activityService, PlayerService playerService) {
        this.activityService = activityService;
        this.playerService = playerService;
    }

    @GetMapping("/shop")
    public String showShop(@RequestParam("playerId") int id,Model model){
        List<Activity> activities=activityService.findAll();
        Player player=playerService.findPlayerById(id);
        model.addAttribute("player",player);
        model.addAttribute("activities",activities);

        return "display-activities";
    }

    @GetMapping("/createActivityForm")
    public String createActivityForm(Model model){
        Activity activity=new Activity();
        model.addAttribute("activity",activity);

        return "create-activity";
    }

    @PostMapping("/createActivity")
    public String createActivity(@ModelAttribute ("activity") Activity activity){
        activityService.saveActivity(activity);
        return "redirect:/";
    }

    @PutMapping("/buy")
    public String buyActivity(@RequestParam ("playerId") int id,@RequestParam ("activityId") int activityId){
        Player player=playerService.findPlayerById(id);
        Activity activity=activityService.findById(activityId);
        if (activityService.buy(player,activityId)){
            return "redirect:/player/choresList?id="+id;
        }
        return "redirect:/player/inventory?playerId="+id;
    }

    @PutMapping("/useActivity")
    public String useActivity(@RequestParam ("playerId") int id,@RequestParam("activityId") int activityId,Principal principal){
        System.out.println("intra in /useActivit put mapping");
        Activity activity=activityService.findById(activityId);
        activityService.useActivity(activity.getName(),principal,id,activityId);
        return "redirect:/player/inventory?playerId="+id;
    }
}
