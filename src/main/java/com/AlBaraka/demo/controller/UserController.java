package com.AlBaraka.demo.controller;



import com.AlBaraka.demo.entity.User;
import com.AlBaraka.demo.service.impl.UserServiceImp;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class UserController {

    private final UserServiceImp userService;

    @PostMapping("/signup")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User createdUser = userService.createUser(user);
        return ResponseEntity.ok(createdUser);
    }
    @GetMapping("/US")
    public List<User> getAll(){
        return userService.getAll();
    }
}
