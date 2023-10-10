package com.letschores.demo.service;

import com.letschores.demo.model.Chore;
import com.letschores.demo.model.Player;
import com.letschores.demo.model.User;
import com.letschores.demo.model.dto.PlayerDTO;
import com.letschores.demo.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

@Service
@EnableScheduling
public class PlayerService {

    private PlayerRepository playerRepository;
    private UserService userService;

    public PlayerService(PlayerRepository playerRepository, UserService userService) {
        this.playerRepository = playerRepository;
        this.userService = userService;
    }

    @Transactional
    public Player createPlayer(PlayerDTO playerDTO){
        Authentication auth=SecurityContextHolder.getContext().getAuthentication();
        User user=userService.findUserByUserName(auth.getName());
        Player player =Player.builder()
                .firstName(playerDTO.getFirstName())
                .lastName(playerDTO.getLastName())
                .coins(0)
                .playerActivities(new ArrayList<>())
                .user(user)
                .build();
        if (user.getPlayer1()==null){
            user.setPlayer1(player);
        } else if (user.getPlayer2()==null) {
            user.setPlayer2(player);
        }
        playerRepository.save(player);
        userService.save(user);

        return player;
    }

    public Player findPlayerById(int id){
        Player player=playerRepository.findPlayerById(id);
        return player;
    }


    @Transactional
    public void save(Player player) {
        playerRepository.save(player);
    }

    @Transactional
    public void removePlayerById(int id) {
        Player player=playerRepository.findById(id).orElseThrow(()->new RuntimeException("Player not found"));

        Authentication auth=SecurityContextHolder.getContext().getAuthentication();
       User user= userService.findUserByUserName(auth.getName());
       if (user.getPlayer1()==player){

           user.getPlayer1().setReviews(null);
           playerRepository.save(player);
           playerRepository.deleteById(id);
           user.setPlayer1(null);
       }else {
           user.getPlayer2().setReviews(null);
           playerRepository.save(player);
           playerRepository.deleteById(id);
           user.setPlayer2(null);
       }
        userService.save(user);
    }

    @Transactional
    public void updatePlayer(int id,Player player) {
        Player updatedPlayer=playerRepository.findPlayerById(id);
        updatedPlayer.setCoins(player.getCoins());
        updatedPlayer.setUser(player.getUser());
        updatedPlayer.setFirstName(player.getFirstName());
        updatedPlayer.setLastName(player.getLastName());
        updatedPlayer.setChores(player.getChores());

        playerRepository.save(updatedPlayer);

    }

    public List<Player> findAll() {
        return playerRepository.findAll();
    }
}
