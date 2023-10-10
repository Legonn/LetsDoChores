package com.letschores.demo.controller;

import com.letschores.demo.model.Player;
import com.letschores.demo.repository.PlayerRepository;
import com.letschores.demo.service.PlayerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class StatisticsController {

    private final PlayerService playerService;



    @GetMapping("/statistics")
    public String getAllPlayersByFirstName(@RequestParam ("name") String name, Model model){
        List<Player> players=new ArrayList<>();
        List<Player> allPlayers=playerService.findAll();
        for (Player player:allPlayers){
            if (player.getFirstName().equalsIgnoreCase(name)){
                players.add(player);
            }
        }
        model.addAttribute("players",players);
        return "admin-search";

    }

    @GetMapping("/statisticsSearch")
    public String statisticsSearch(){
        return "statistics-search";
    }
}
