package com.AlBaraka.demo.entity;

import com.AlBaraka.demo.entity.enums.UserRole;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;


@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    private String fullName;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Builder.Default
    private boolean active = true;


    private LocalDateTime createdAt = LocalDateTime.now();

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
