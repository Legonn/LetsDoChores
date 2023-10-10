package com.letschores.demo.model.dto;

import com.letschores.demo.model.Chore;
import com.letschores.demo.model.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.util.List;

@Data
public class PlayerDTO {


    @Pattern(regexp = "^(?![ -`]*$)[A-Z][-`A-Za-z]{1,}$",message = "Invalid First Name, it should be at least 2 characters long, it can't contain numbers and it should start with a capital letter")
    private String firstName;
    @Pattern(regexp = "^(?![ -`]*$)[A-Z][-`A-Za-z]{1,}$",message = "Invalid Last Name, it should be at least 2 characters long, it can't contain numbers and it should start with a capital letter")
    private String lastName;
    private int coins;
    private List<Chore> chores;
    private int userId;



}
