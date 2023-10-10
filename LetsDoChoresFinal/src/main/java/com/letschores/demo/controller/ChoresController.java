package com.letschores.demo.controller;

import com.letschores.demo.model.Chore;
import com.letschores.demo.model.Player;
import com.letschores.demo.model.dto.ChoreDTO;
import com.letschores.demo.service.ChoreService;
import com.letschores.demo.service.PlayerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/chores")
@Validated
public class ChoresController {

    private final ChoreService choreService;
    private final PlayerService playerService;

    @Autowired
    public ChoresController(ChoreService choreService, PlayerService playerService) {
        this.choreService = choreService;
        this.playerService = playerService;
    }

    @GetMapping("/createChoresForm")
    public String createChoresForm(Model model,@RequestParam ("playerId") int id){

        Chore chore = new Chore();
        model.addAttribute("chore",chore);
        model.addAttribute("playerId",id);
        return "create-chore";
    }

    @PostMapping("/createChores")
    @ResponseStatus(HttpStatus.CREATED)
    public void processingCreateChores(@Valid @ModelAttribute("chore") ChoreDTO choreDTO,
                                       @RequestParam("playerId") int id ){

        Player player = playerService.findPlayerById(id);

            try{
             Chore chore  = choreService.createChore(choreDTO);
                player.getChores().add(chore);
            }catch (Exception e){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Invalid Data, try again",e);
            }

    }

    @GetMapping("/done")
    public String updateChoreStatus(@RequestParam("id") int id){
       choreService.updateChoreStatus(id);
       return "redirect:/chores/congratulations";
    }




    @GetMapping("/congratulations")
    public String showCongratulations(){
        return "chores-congratulations";
    }


    @DeleteMapping("/deleteChore")
    public String deleteChore(@RequestParam("id") int id){
        System.out.println("intra in delete");
        choreService.deleteById(id);

        return "redirect:/viewAllChores";
    }

    @GetMapping("/updateChore")
    public String updateChore(@RequestParam("id") int id,Model model){
        Chore chore=choreService.findById(id);
        model.addAttribute("chore",chore);
        return "update-chore";
    }

    @PutMapping("/processUpdateChore")
    public String processUpdateChore(@ModelAttribute ("chore") Chore chore){
        choreService.updateChore(chore);
       return "redirect:/viewAllChores";
    }

}
