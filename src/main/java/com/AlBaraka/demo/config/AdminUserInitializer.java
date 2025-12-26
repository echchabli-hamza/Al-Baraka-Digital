package com.AlBaraka.demo.config;

import com.AlBaraka.demo.entity.User;
import com.AlBaraka.demo.entity.enums.UserRole;
import com.AlBaraka.demo.repository.UserRepo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Component
@RequiredArgsConstructor
public class AdminUserInitializer implements CommandLineRunner {

    private final UserRepo userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (!userRepository.existsByEmail("admin")) {
            User admin = new User();
            admin.setEmail("admin");
            admin.setPassword(passwordEncoder.encode("admin"));
            admin.setRole(UserRole.valueOf("ROLE_ADMIN"));
            admin.setActive(true);
            userRepository.save(admin);
            System.out.println("Admin user created with hashed password.");
        } else {
            System.out.println("Admin user already exists.");
        }

        if (!userRepository.existsByEmail("client1")) {
            User admin = new User();
            admin.setEmail("client1");
            admin.setPassword(passwordEncoder.encode("client1"));
            admin.setRole(UserRole.valueOf("ROLE_CLIENT"));
            admin.setActive(true);
            userRepository.save(admin);
            System.out.println("Admin user created with hashed password.");
        } else {
            System.out.println("Admin user already exists.");
        }

        if (!userRepository.existsByEmail("client2")) {
            User admin = new User();
            admin.setEmail("client2");
            admin.setPassword(passwordEncoder.encode("client2"));
            admin.setRole(UserRole.valueOf("ROLE_CLIENT"));
            admin.setActive(true);
            userRepository.save(admin);
            System.out.println("Admin user created with hashed password.");
        } else {
            System.out.println("Admin user already exists.");
        }
    }
}
