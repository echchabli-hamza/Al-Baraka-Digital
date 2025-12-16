package com.AlBaraka.demo.service.impl;

import com.AlBaraka.demo.entity.User;
import com.AlBaraka.demo.repository.UserRepo;
import com.AlBaraka.demo.service.inter.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserServiceImp {


    private  final  UserRepo ur;

    private final PasswordEncoder passwordEncoder;

    public UserServiceImp(UserRepo ur, PasswordEncoder passwordEncoder) {
        this.ur = ur;
        this.passwordEncoder = passwordEncoder;
    }


    public User createUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setActive(true);
        user.setCreatedAt(LocalDateTime.now());
        return ur.save(user);
    }

    public List<User> getAll(){
        return ur.findAll();
    }

}
