package com.AlBaraka.demo.dto;

import com.AlBaraka.demo.entity.enums.OperationType;
import com.AlBaraka.demo.entity.enums.Status;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class CreateOperationRequest {

    private Long id;
    private OperationType type;
    private BigDecimal amount;
    private String description;
    private LocalDateTime createdAt;
    private Status status;
    private String SourceAccountNumber;
    private String DestinationAccountNumber;


}


