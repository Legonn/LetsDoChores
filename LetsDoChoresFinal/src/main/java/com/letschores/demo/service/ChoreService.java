package com.letschores.demo.service;

import com.letschores.demo.model.Chore;
import com.letschores.demo.model.Player;
import com.letschores.demo.model.Review;
import com.letschores.demo.model.User;
import com.letschores.demo.model.dto.ChoreDTO;
import com.letschores.demo.repository.ChoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;


@Service
public class ChoreService {

    private ChoreRepository choreRepository;
    private final PlayerService playerService;
    private UserService userService;


    @Autowired
    public ChoreService(ChoreRepository choreRepository, PlayerService playerService, UserService userService) {
        this.choreRepository = choreRepository;
        this.playerService = playerService;
        this.userService = userService;
    }

    public Chore findById(int id) {
        return choreRepository.findById(id);
    }

    @Transactional
    public void updateChoreStatus(int id) {
        Chore chore = choreRepository.findById(id);
        chore.setDone(true);
        choreRepository.save(chore);
    }



    @Transactional
    public Chore createChore(ChoreDTO choreDTO) {

        Player player = playerService.findPlayerById(choreDTO.getPlayerId());
        Chore chore = Chore.builder()
                .description(choreDTO.getDescription())
                .duration(choreDTO.getDuration())
                .difficulty(choreDTO.getDifficulty())
                .isDone(false)
                .date(LocalDate.now())
                .player(player)
                .build();
        int value = getValue(chore);
        chore.setValue(value);
        choreRepository.save(chore);

        return chore;
    }

    public static int getValue(Chore chore) {
        int value = 0;
        if (chore.getDifficulty().equals("EASY")) {
            value = chore.getDuration() * 1;
        } else if (chore.getDifficulty().equals("MEDIUM")) {
            value = chore.getDuration() * 2;
        } else {
            value = chore.getDuration() * 3;
        }
        return value;
    }


    @Transactional
    public void save(Chore chore) {
        choreRepository.save(chore);
    }

    @Scheduled(fixedRate = 10* 60 * 1000)
    @Transactional
    public void updateChores(){
        System.out.println("start");

        List<User> users= userService.findAllUsers();
        for (User user:users){
            if(user.getPlayer1() !=null && user.getPlayer2() !=null){
                Player player1=user.getPlayer1();
                Player player2=user.getPlayer2();

                List<Chore> player1Chores=player1.getChores();
                List<Chore> player2Chores=player2.getChores();



                List<Chore> choreList=new ArrayList<>();

                choreList.addAll(player1Chores);
                choreList.addAll(player2Chores);




                for (Chore chore : choreList) {
                    chore.setDone(false);
                    chore.setHasBeenReviewed(false);
                }

                if (choreList.size() %2 !=0){
                    choreList.add(null);
                }
                Collections.shuffle(choreList);

                player1Chores= new ArrayList<>(choreList.subList(0, choreList.size() / 2));

                player2Chores = new ArrayList<>(choreList.subList(choreList.size() / 2, choreList.size()));

                if (player1Chores.contains(null)){
                    player1Chores.remove(null);
                } else if (player2Chores.contains(null)) {
                    player2Chores.remove(null);
                }

                player1.setChores(player1Chores);
                player2.setChores(player2Chores);



                playerService.save(player1);
                playerService.save(player2);

                for (Chore chore: player1.getChores()){
                    chore.setPlayer(player1);
                    chore.setDate(LocalDate.now());
                    choreRepository.save(chore);
                }
                for (Chore chore: player2.getChores()){
                    chore.setPlayer(player2);
                    chore.setDate(LocalDate.now());
                    choreRepository.save(chore);
                }
            }
        }
    }

    @Transactional
    public void deleteById(int id) {

        Authentication auth= SecurityContextHolder.getContext().getAuthentication();
        User user=userService.findUserByUserName(auth.getName());
        Player player=new Player();
        if (user.getPlayer1().getChores().contains(choreRepository.findById(id))){
            player=user.getPlayer1();
        } else if (user.getPlayer2().getChores().contains(choreRepository.findById(id))) {
            player=user.getPlayer2();
        }
        if (player !=null){
            Chore chore=choreRepository.findById(id);
            for (Review review:chore.getReviews()){
                review.setChore(null);
            }

            player.getChores().remove(chore);
            playerService.save(player);
        }
        choreRepository.deleteById(id);


    }

    @Transactional
    public void updateChore(Chore chore) {
        Chore tempChore=choreRepository.findById(chore.getId());
        tempChore.setDate(LocalDate.now());
        tempChore.setDone(chore.isDone());
        tempChore.setPlayer(chore.getPlayer());
        tempChore.setHasBeenReviewed(chore.isHasBeenReviewed());
        tempChore.setDescription(chore.getDescription());
        tempChore.setDuration(chore.getDuration());
        tempChore.setDifficulty(chore.getDifficulty());

        tempChore.setValue(getValue(tempChore));

        choreRepository.save(tempChore);
    }
}
