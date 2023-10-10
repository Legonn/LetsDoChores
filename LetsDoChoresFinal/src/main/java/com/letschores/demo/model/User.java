package com.letschores.demo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name="users")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false, unique = true)
    @NotBlank
    @Email
    private String userName;
    @Pattern(regexp = "^(?=.*\\d).{8,}$",message = "Password should be at least 8 characters long and it should contain at least 1 digit")
    private String password;
    private String role;
    @OneToOne
    @JoinColumn(name="player1_id", referencedColumnName = "id")
    private Player player1;
    @OneToOne
    @JoinColumn(name="player2_id", referencedColumnName = "id")
    private Player player2;

}
