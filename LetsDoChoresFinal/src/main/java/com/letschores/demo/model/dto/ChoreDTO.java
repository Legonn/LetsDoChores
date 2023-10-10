package com.letschores.demo.model.dto;

import com.letschores.demo.model.Chore;
import com.letschores.demo.model.Player;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ChoreDTO {


    @NotBlank(message = "Description field shouldn't be empty")
    private String description;
    @Max(value = 31)
    private int duration;
    private String difficulty;
    private int playerId;
}
