package com.AlBaraka.demo.service.impl;

import com.AlBaraka.demo.entity.Account;
import com.AlBaraka.demo.entity.User;
import com.AlBaraka.demo.entity.enums.UserRole;
import com.AlBaraka.demo.repository.AccountRepository;
import com.AlBaraka.demo.repository.UserRepo;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserServiceImp {

    private final UserRepo userRepo;
    private final AccountRepository accountRepo;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImp(UserRepo userRepo, AccountRepository accountRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.accountRepo = accountRepo;
        this.passwordEncoder = passwordEncoder;
    }


    public User createUser(User user) {

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setActive(true);
        user.setCreatedAt(LocalDateTime.now());


        user.setRole(UserRole.ROLE_CLIENT);
        User savedUser = userRepo.save(user);


        Account account = new Account();
        account.setOwner(savedUser);
        account.setBalance(BigDecimal.valueOf(0.0));
        account.setAccountNumber(generateUniqueAccountNumber());

        accountRepo.save(account);

        return savedUser;
    }


    public List<User> getAll() {
        return userRepo.findAll();
    }


    public void setUserActiveStatus(Long userId, boolean active) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setActive(active);
        userRepo.save(user);


        List<Account> accounts = accountRepo.findAllByOwner(user);
        accounts.forEach(account -> {
            account.setBalance(account.getBalance());
            accountRepo.save(account);
        });
    }

    private String generateUniqueAccountNumber() {
        String accountNumber;
        do {
            accountNumber = "AB" + (int)(Math.random() * 1_000_000_000);
        } while (accountRepo.existsByAccountNumber(accountNumber));
        return accountNumber;
    }
}

