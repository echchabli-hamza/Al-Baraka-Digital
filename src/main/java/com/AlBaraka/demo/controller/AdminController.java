package com.AlBaraka.demo.controller;

import com.AlBaraka.demo.dto.RegisterRequest;
import com.AlBaraka.demo.entity.User;
import com.AlBaraka.demo.exeption.ClientNotFoundException;
import com.AlBaraka.demo.service.impl.UserServiceImp;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final UserServiceImp userService;

    public AdminController(UserServiceImp userService) {
        this.userService = userService;
    }


    @PostMapping("/users")
    public ResponseEntity<User> createUser(@RequestBody RegisterRequest request) {

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
           user.setActive(request.isActive());
        User createdUser = userService.createUser(user);
        return ResponseEntity.ok(createdUser);
    }


    @PutMapping("/users/{id}")
    public ResponseEntity<User> updateUser(
            @PathVariable Long id,
            @RequestBody User user
    ) {
        user.setId(id);
        User updatedUser = userService.createUser(user);
        return ResponseEntity.ok(updatedUser);
    }


    @DeleteMapping("/users/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        userService.setUserActiveStatus(id, false);
        return ResponseEntity.ok("User with id " + id + " has been deactivated");
    }


    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAll();
        return ResponseEntity.ok(users);
    }


    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = userService.getAll().stream()
                .filter(u -> u.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ClientNotFoundException("User not found"));
        return ResponseEntity.ok(user);
    }
}
