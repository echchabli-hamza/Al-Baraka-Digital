package com.AlBaraka.demo.service.impl;

import com.AlBaraka.demo.dto.OperationResponse;
import com.AlBaraka.demo.entity.Account;
import com.AlBaraka.demo.entity.Operation;
import com.AlBaraka.demo.entity.enums.Status;
import com.AlBaraka.demo.repository.AccountRepository;
import com.AlBaraka.demo.repository.OperationRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AgentOperationService {

    private final OperationRepository operationRepository;
    private final AccountRepository accountRepository;

    public List<OperationResponse> getPendingOperations() {

        return operationRepository.findByStatus(Status.PENDING)
                .stream()
                .map(this::mapToAgentResponse)
                .toList();
    }


    public void approveOperation(Long operationId) {

        Operation operation = operationRepository.findById(operationId)
                .orElseThrow(() -> new RuntimeException("Operation not found"));

        if (!operation.getStatus().equals(Status.PENDING.name())) {

            throw new RuntimeException("Operation already processed");
        }

        switch (operation.getType()) {

            case DEPOSIT -> handleDeposit(operation);

            case WITHDRAWAL -> handleWithdrawal(operation);

            case TRANSFER -> handleTransfer(operation);
        }

        operation.setStatus(Status.APPROVED.name());
        operation.setValidatedAt(LocalDateTime.now());
        operation.setExecutedAt(LocalDateTime.now());

        operationRepository.save(operation);
    }

    public void rejectOperation(Long operationId) {

        Operation operation = operationRepository.findById(operationId)
                .orElseThrow(() -> new RuntimeException("Operation not found"));

        if (operation.getStatus().equals(Status.PENDING.name())) {
            throw new RuntimeException("Operation already processed");
        }

        operation.setStatus(Status.REJECT.name());
        operation.setValidatedAt(LocalDateTime.now());

        operationRepository.save(operation);
    }


    private void handleDeposit(Operation operation) {
        Account account = operation.getAccountDestination();
        account.setBalance(account.getBalance().add(operation.getAmount()));
        accountRepository.save(account);
    }

    private void handleWithdrawal(Operation operation) {
        Account account = operation.getAccountSource();

        if (account.getBalance().compareTo(operation.getAmount()) < 0) {
            throw new RuntimeException("Insufficient balance");
        }

        account.setBalance(account.getBalance().subtract(operation.getAmount()));
        accountRepository.save(account);
    }

    private void handleTransfer(Operation operation) {

        Account source = operation.getAccountSource();
        Account destination = operation.getAccountDestination();

        if (source.getBalance().compareTo(operation.getAmount()) < 0) {
            throw new RuntimeException("Insufficient balance");
        }

        source.setBalance(source.getBalance().subtract(operation.getAmount()));
        destination.setBalance(destination.getBalance().add(operation.getAmount()));

        accountRepository.save(source);
        accountRepository.save(destination);
    }






    private OperationResponse mapToAgentResponse(Operation operation) {

        OperationResponse dto = new OperationResponse();

        dto.setId(operation.getId());
        dto.setType(operation.getType());
        dto.setAmount(operation.getAmount());
        dto.setStatus(Status.valueOf(operation.getStatus()));
        dto.setCreatedAt(operation.getCreatedAt());

        if (operation.getAccountSource() != null) {
            dto.setSourceAccountNumber(
                    operation.getAccountSource().getAccountNumber()
            );
        }

        if (operation.getAccountDestination() != null) {
            dto.setDestinationAccountNumber(
                    operation.getAccountDestination().getAccountNumber()
            );
        }

        dto.setHasDocument(operation.getDocument() != null);

        return dto;
    }






    public Operation getOperationById(Long operationId) {
        Operation operation = operationRepository.findById(operationId)
                .orElseThrow(() -> new RuntimeException("Operation not found"));




        return operation;
    }






}
