package com.letschores.demo.controller;

import com.letschores.demo.model.*;
import com.letschores.demo.model.dto.ChoreDTO;
import com.letschores.demo.model.dto.PlayerDTO;
import com.letschores.demo.service.ChoreService;
import com.letschores.demo.service.PlayerService;
import com.letschores.demo.service.ReviewService;
import com.letschores.demo.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/player")
@Validated
public class PlayerController {

    private final PlayerService playerService;
    private UserService userService;
    private ReviewService reviewService;

    @Autowired
    public PlayerController(PlayerService playerService, UserService userService, ReviewService reviewService) {
        this.playerService = playerService;
        this.userService = userService;
        this.reviewService = reviewService;
    }

    @GetMapping("/choresList")
    public String getChores(@RequestParam("id") int id, Model model) {
        Player player = playerService.findPlayerById(id);

        List<Chore> chores = player.getChores();
        model.addAttribute("chores", chores);
        model.addAttribute("player", player);
        return "display-chores";
    }

    @GetMapping("/choresToReview")
    public String reviewChores(@RequestParam("id") int id, Model model) {
        Player player = playerService.findPlayerById(id);
        List<Chore> chores = new ArrayList<>();
        Authentication auth= SecurityContextHolder.getContext().getAuthentication();
        User user=userService.findUserByUserName(auth.getName());

        if (user.getPlayer1().getId() != id) {
            chores = user.getPlayer1().getChores();
        } else {
            chores = user.getPlayer2().getChores();
        }
        model.addAttribute("chores", chores);
        model.addAttribute("player",player);

        return "review-chores";
    }

    @GetMapping("/test")
    public String test() {
        return "redirect:/home";
    }

    @GetMapping("/createPlayer")
    public String createPlayerForm(Model model, @RequestParam("userId") int id, Principal principal) {
        Player player = new Player();
        User user=userService.findUserByUserName(principal.getName());
        model.addAttribute("user",user);
        model.addAttribute("player", player);
        model.addAttribute("userId", id);

        return "create-player";
    }
    @GetMapping("/redirect")
    public String redirectToCreateChoresForm(@RequestParam ("id") int id){
        User user=userService.findUserById(id);
        if (user.getPlayer1() !=null && user.getPlayer2()==null){
            return "redirect:/chores/createChoresForm?playerId="+user.getPlayer1().getId();
        } else if (user.getPlayer2()!= null) {
            if (user.getPlayer2().getChores().size()<=user.getPlayer1().getChores().size()){
                return "redirect:/chores/createChoresForm?playerId="+user.getPlayer2().getId();
            } else if (user.getPlayer2().getChores().size()>=user.getPlayer1().getChores().size() ){
                return "redirect:/chores/createChoresForm?playerId="+user.getPlayer1().getId();
            }
        }
        return "redirect:/";
    }

    @GetMapping("/updatePlayer")
    public String showFormForUpdate(@RequestParam("playerId") int id, Model model) {
        Player player = playerService.findPlayerById(id);
        model.addAttribute("player", player);

        return "update-player";
    }

    @PutMapping("/processUpdate")
    public String processUpdate(@RequestParam("playerId") int id,@ModelAttribute("player") Player player){
        playerService.updatePlayer(id,player);
        return "redirect:/player/findPlayerById?playerId="+id;
    }

    @PostMapping("/createPlayer")
    @ResponseStatus(HttpStatus.CREATED)
    public void processingCreatePlayer(@Valid @ModelAttribute("player") PlayerDTO playerDTO) {

        try {
            Player player = playerService.createPlayer(playerDTO);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Invalid Data, Try again",e);
        }
    }

    @GetMapping("/gainCoins")
    public String getCoins(@RequestParam("reviewId") int reviewId){
        Review review=reviewService.findById(reviewId);
        Player player=review.getChore().getPlayer();
        int currentCoins=player.getCoins();
        int increasedValue=currentCoins+review.getValue();
        if (review.isCompleted()){
            player.setCoins(increasedValue);
        }
        playerService.save(player);
        return "redirect:/";
    }

    @GetMapping("/findPlayerById")
    public String findPlayer(@RequestParam ("playerId") int id,Model model){
       Player player= playerService.findPlayerById(id);
       model.addAttribute("player",player);
       return "display-player";
    }

    @DeleteMapping("/deletePlayerById")
    public String deletePlayerById(@RequestParam ("playerId") int id){
        System.out.println("intra in delete");
        playerService.removePlayerById(id);

        return "redirect:/";
    }

    @GetMapping("/inventory")
    public String inventory(@RequestParam ("playerId") int id,Model model){
        Player player=playerService.findPlayerById(id);
        List<Activity> activities=player.getPlayerActivities();
        model.addAttribute("player",player);
        model.addAttribute("activities",activities);
        return "inventory";
    }




}
