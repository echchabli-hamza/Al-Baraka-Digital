package com.AlBaraka.demo.entity;


import com.AlBaraka.demo.entity.enums.OperationType;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "operations")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Operation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private OperationType type;

    @Column(nullable = false)
    private BigDecimal amount;

    private String status;

    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime validatedAt;
    private LocalDateTime executedAt;

    @ManyToOne
    @JoinColumn(name = "account_source_id")
    private Account accountSource;

    @ManyToOne
    @JoinColumn(name = "account_destination_id")
    private Account accountDestination;

    @OneToOne(mappedBy = "operation", cascade = CascadeType.ALL, orphanRemoval = true)
    private Document document;

}
