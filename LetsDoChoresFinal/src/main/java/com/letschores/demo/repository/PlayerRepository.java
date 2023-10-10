package com.letschores.demo.repository;

import com.letschores.demo.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Integer> {

    public Player findPlayerById(int id);
}
