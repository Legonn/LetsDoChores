package com.letschores.demo.service;

import com.letschores.demo.model.Activity;
import com.letschores.demo.model.Chore;
import com.letschores.demo.model.Player;
import com.letschores.demo.model.User;
import com.letschores.demo.repository.ActivityRepository;
import org.hibernate.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class ActivityService {

    private ActivityRepository activityRepository;
    private UserService userService;
    private PlayerService playerService;
    private ChoreService choreService;


    @Autowired
    public ActivityService(ActivityRepository activityRepository, UserService userService, PlayerService playerService, ChoreService choreService) {
        this.activityRepository = activityRepository;
        this.userService = userService;
        this.playerService = playerService;
        this.choreService = choreService;
    }

    public List<Activity> findAll(){
        return activityRepository.findAll();
    }

    @Transactional
    public void saveActivity(Activity activity){
        activityRepository.save(activity);
    }

    @Transactional
    public boolean buy(Player player,int activityId){
        Activity activity= activityRepository.findById(activityId).orElseThrow(()->new RuntimeException("Activity not found"));
        if (activity.getPlayers()==null){
            activity.setPlayers(new ArrayList<>());
        }
        if (player.getCoins() >=activity.getCoins()){
            int currentCoins=player.getCoins();
            player.setCoins(currentCoins-activity.getCoins());
            player.getPlayerActivities().add(activity);
            activityRepository.save(activity);
            return true;
        }else {
            return false;
        }
    }

    public Activity findById(int activityId) {
       Activity activity= activityRepository.findById(activityId).orElseThrow(()->new RuntimeException("Activity not found"));
        return activity;
    }

    @Transactional
    public void useActivity(String name,Principal principal,int playerId,int activityId) {
        User user=userService.findUserByUserName(principal.getName());
        Player player1=user.getPlayer1();
        Player player2=user.getPlayer2();
        if (name.equalsIgnoreCase("Swap Chores")){
            useSwapChoresActivity(principal,playerId,activityId);
        }

    }

    @Transactional
    private void useSwapChoresActivity(Principal principal,int playerId,int activityId) {
        User user=userService.findUserByUserName(principal.getName());
        Activity activity=activityRepository.findById(activityId).orElseThrow(()->new RuntimeException("Activity not found"));
        Player player1;
        Player player2;
        if (user.getPlayer1().getId()==playerId){
             player1=user.getPlayer1();
             player2=user.getPlayer2();
        }else {
             player1=user.getPlayer2();
             player2=user.getPlayer1();
        }

        if (player1.getPlayerActivities().contains(activity)){
            System.out.println("intra in if");
            List<Chore> tempChores=new ArrayList<>(player1.getChores());
            player1.setChores(player2.getChores());
            player2.setChores(tempChores);
            player1.getPlayerActivities().remove(activity);

            playerService.save(player1);
            playerService.save(player2);
            for (Chore chore: player1.getChores()){
                chore.setPlayer(player1);
                chore.setDate(LocalDate.now());
                choreService.save(chore);
            }
            for (Chore chore: player2.getChores()){
                chore.setPlayer(player2);
                chore.setDate(LocalDate.now());
                choreService.save(chore);
            }
        }

    }
}
