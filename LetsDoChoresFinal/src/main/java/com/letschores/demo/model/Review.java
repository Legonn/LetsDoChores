package com.letschores.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Entity
@Builder
@Table (name="reviews")
@AllArgsConstructor
@NoArgsConstructor
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String description;
    private int value;
    private int duration;
    private String difficulty;
    private boolean isCompleted;
    private String comment;
    private LocalDate localDate;
    @ManyToOne
    @JoinColumn(name="chore_id",referencedColumnName = "id")
    private Chore chore;
    @ManyToOne
    @JoinColumn(name="player_id",nullable = false)
    private Player player;


}
