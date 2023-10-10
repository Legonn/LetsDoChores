package com.letschores.demo.service;

import com.letschores.demo.configuration.MyUserDetails;
import com.letschores.demo.model.Chore;
import com.letschores.demo.model.Player;
import com.letschores.demo.model.User;
import com.letschores.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    private  UserRepository userRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    @Autowired
    public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public MyUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user=userRepository.findByUserName(username).orElseThrow(()->new UsernameNotFoundException("user not found"));

        return new MyUserDetails(user);
    }

    @Transactional
    public User createUser(User user){
        User newUser=User.builder()
                .userName(user.getUserName())
                .password(bCryptPasswordEncoder.encode(user.getPassword()))
                .role("ROLE_USER")
                .build();
        userRepository.save(newUser);

        return newUser;
    }

    public User findUserByUserName(String username){
        User user=userRepository.findByUserName(username).orElseThrow(()->new UsernameNotFoundException("user not found"));

        return user;
    }
    public User findUserById(int id){
        User user=userRepository.findById(id).orElseThrow(()->new UsernameNotFoundException("user not found"));
        return user;
    }

    public List<User> findAllUsers(){
       List<User> users=userRepository.findAll();
       return users;
    }

    @Transactional
    public void save(User user) {
        userRepository.save(user);
    }
}
