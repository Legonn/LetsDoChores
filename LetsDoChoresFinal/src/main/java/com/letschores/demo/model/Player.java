package com.letschores.demo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.List;

@Data
@Entity
@Table(name = "players")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String firstName;
    private String lastName;
    private int coins;
    @OneToMany(mappedBy = "player", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Chore> chores;
    @OneToMany(mappedBy = "player", cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private List<Review> reviews;
    @OneToOne(mappedBy = "player1")
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToMany
    @JoinTable(
            name="player_activities",
            joinColumns = @JoinColumn(name="player_id"),
            inverseJoinColumns = @JoinColumn(name="activity_id")
    )
    private List<Activity> playerActivities;


    @Override
    public String toString() {
        return "Player{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", coins=" + coins +
                ", chores=" + chores +
                '}';
    }
}
