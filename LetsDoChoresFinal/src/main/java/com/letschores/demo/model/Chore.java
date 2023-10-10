package com.letschores.demo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;


@Data
@Entity
@Table(name="chores")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Chore {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String description;
    private int value;
    private int duration;
    private String difficulty;
    private boolean isDone;
    private boolean hasBeenReviewed;
    private LocalDate date;
    @OneToMany(mappedBy = "chore",cascade =CascadeType.ALL,fetch = FetchType.EAGER)
    private List<Review> reviews;
    @ManyToOne
    @JoinColumn(name="player_id")
    private Player player;


    @Override
    public String toString() {
        return "Chore{" +
                "description='" + description + '\'' +
                ", value=" + value +
                ", duration=" + duration +
                ", difficulty='" + difficulty + '\'' +
                ", isDone=" + isDone +
                '}';
    }
}
