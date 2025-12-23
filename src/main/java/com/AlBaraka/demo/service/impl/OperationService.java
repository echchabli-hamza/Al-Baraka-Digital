package com.AlBaraka.demo.service.impl;

import com.AlBaraka.demo.dto.CreateOperationRequest;
import com.AlBaraka.demo.dto.OperationResponse;
import com.AlBaraka.demo.entity.Account;
import com.AlBaraka.demo.entity.Document;
import com.AlBaraka.demo.entity.Operation;
import com.AlBaraka.demo.entity.enums.OperationType;
import com.AlBaraka.demo.entity.enums.Status;
import com.AlBaraka.demo.repository.AccountRepository;
import com.AlBaraka.demo.repository.OperationRepository;
import io.jsonwebtoken.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.aspectj.runtime.internal.Conversions.doubleValue;

@Service
@RequiredArgsConstructor
public class OperationService {

    private final OperationRepository operationRepository;
    private final AccountRepository accountRepository;


    @Value("${file.upload-dir}")
    private String uploadDir1;


    private static final BigDecimal AUTO_APPROVE_LIMIT = new BigDecimal("10000");

    @Transactional
    public OperationResponse createDeposit(Long userId, CreateOperationRequest request) {


        Account account = accountRepository.findByOwnerId(userId);

        Optional<Account> accountD = accountRepository.findByAccountNumber(request.getDestinationAccountNumber());

        if (accountD.isEmpty()) {
            throw new RuntimeException("the account numer doesn't exist in db");
        }


        Account ad = accountD.get();

        if (ad.getBalance().compareTo(request.getAmount()) < 0) {

            throw new RuntimeException("you dont have enough balance ");
        }

        Operation operation = Operation.builder()
                .type(OperationType.DEPOSIT)
                .amount(request.getAmount())
                .accountSource(account)
                .accountDestination(ad)
                .createdAt(LocalDateTime.now())
                .build();


        if (request.getAmount().compareTo(AUTO_APPROVE_LIMIT) <= 0) {

            operation.setStatus("APPROVED");
            operation.setValidatedAt(LocalDateTime.now());
            operation.setExecutedAt(LocalDateTime.now());


            account.setBalance(account.getBalance().add(request.getAmount()));
            accountRepository.save(account);
        } else {
            operation.setStatus("PENDING");

            Operation saved = operationRepository.save(operation);


        }

        Operation saved = operationRepository.save(operation);
        return mapToResponse(saved);
    }

    @Transactional
    public OperationResponse createWithdrawal(Long userId, CreateOperationRequest request) {


        Account account = accountRepository.findByOwnerId(userId);


        if (account.getBalance().compareTo(request.getAmount()) < 0) {
            throw new RuntimeException("Insufficient balance");
        }


        Operation operation = Operation.builder()
                .type(OperationType.WITHDRAWAL)
                .amount(request.getAmount())
                .accountSource(account)
                .createdAt(LocalDateTime.now())
                .build();


        if (request.getAmount().compareTo(AUTO_APPROVE_LIMIT) <= 0) {

            operation.setStatus("APPROVED");
            operation.setValidatedAt(LocalDateTime.now());
            operation.setExecutedAt(LocalDateTime.now());

            account.setBalance(account.getBalance().subtract(request.getAmount()));
            accountRepository.save(account);
        } else {

            if (operation.getDocument() == null) {
                throw new RuntimeException("Document required for operations above 10000 DH");
            }
            operation.setStatus("PENDING");
        }

        Operation saved = operationRepository.save(operation);
        return mapToResponse(saved);
    }

    @Transactional
    public OperationResponse createTransfer(Long userId, CreateOperationRequest request) {

        Account sourceAccount = accountRepository.findByOwnerId(userId);


        if (sourceAccount.getBalance().compareTo(request.getAmount()) < 0) {
            throw new RuntimeException("Insufficient balance");
        }


        Optional<Account> destinationAccount = accountRepository.findByAccountNumber(request.getDestinationAccountNumber());

        if (destinationAccount.isEmpty()) {
            throw new RuntimeException("the account numer doesn't exist in db");
        }
        Account ad = destinationAccount.get();

        Operation operation = Operation.builder()
                .type(OperationType.TRANSFER)
                .amount(request.getAmount())
                .accountSource(sourceAccount)
                .accountDestination(ad)
                .createdAt(LocalDateTime.now())
                .build();


        if (request.getAmount().compareTo(AUTO_APPROVE_LIMIT) <= 0) {

            operation.setStatus("APPROVED");
            operation.setValidatedAt(LocalDateTime.now());
            operation.setExecutedAt(LocalDateTime.now());


            sourceAccount.setBalance(sourceAccount.getBalance().subtract(request.getAmount()));
            ad.setBalance(ad.getBalance().add(request.getAmount()));
            accountRepository.save(sourceAccount);
            accountRepository.save(ad);

        } else {

            if (operation.getDocument() == null) {
                throw new RuntimeException("Document required for operations above 10000 DH");
            }
            operation.setStatus("PENDING");

        }

        Operation saved = operationRepository.save(operation);

        return mapToResponse(saved);

    }

    public List<OperationResponse> getClientOperations(Long userId) {
        Account account = accountRepository.findByOwnerId(userId);

        List<Operation> operations = operationRepository
                .findByAccountSourceOrAccountDestination(account, account);

        return operations.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public Operation getOperationById(Long operationId, Long userId) {
        Operation operation = operationRepository.findById(operationId)
                .orElseThrow(() -> new RuntimeException("Operation not found"));


        if (!isOperationOwner(operation, userId)) {
            throw new RuntimeException("Unauthorized access to operation");
        }

        return operation;
    }

    @Transactional
    public void deleteOperation(Long operationId, Long userId) {
        Operation operation = operationRepository.findById(operationId)
                .orElseThrow(() -> new RuntimeException("Operation not found"));


        if (!isOperationOwner(operation, userId)) {
            throw new RuntimeException("Unauthorized: You can only delete your own operations");
        }


        if (!"PENDING".equals(operation.getStatus())) {
            throw new RuntimeException("Can only delete pending operations");
        }

        operationRepository.delete(operation);
    }


    private boolean isOperationOwner(Operation operation, Long userId) {
        return operation.getAccountSource() != null &&
                operation.getAccountSource().getOwner().getId().equals(userId);
    }

    private OperationResponse mapToResponse(Operation operation) {
        OperationResponse response = new OperationResponse();
        response.setId(operation.getId());
        response.setType(operation.getType());
        response.setAmount(operation.getAmount());
        response.setStatus(Status.valueOf(operation.getStatus()));
        response.setCreatedAt(operation.getCreatedAt());


        if (operation.getAccountSource() != null) {
            response.setSourceAccountNumber(operation.getAccountSource().getAccountNumber());
        }

        if (operation.getAccountDestination() != null) {
            response.setDestinationAccountNumber(operation.getAccountDestination().getAccountNumber());
        }

        return response;
    }


    @Transactional
    public void uploadDocument(Long operationId, Long userId, MultipartFile file) {

        Operation operation = operationRepository.findById(operationId)
                .orElseThrow(() -> new RuntimeException("Operation not found"));

        if (!operation.getAccountSource().getOwner().getId().equals(userId)) {
            throw new RuntimeException("Unauthorized");
        }

        if (!"application/pdf".equalsIgnoreCase(file.getContentType())) {
            throw new RuntimeException("Only PDF allowed");
        }

        if (operation.getDocument() != null) {
            throw new RuntimeException("Document already uploaded");
        }

        try {

            String baseDir = "/home/hamza/Downloads/demo/demo/uploads";
            Path uploadDir = Paths.get(baseDir, String.valueOf(operationId));
            Files.createDirectories(uploadDir);

            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path filePath = uploadDir.resolve(fileName);
            file.transferTo(filePath.toFile());


            Document document = Document.builder()
                    .fileName(fileName)
                    .fileType(file.getContentType())
                    .storagePath(filePath.toString())
                    .uploadedAt(LocalDateTime.now())
                    .operation(operation)
                    .build();

            operation.setDocument(document);

            operationRepository.save(operation);


        } catch (IOException | java.io.IOException e) {
            throw new RuntimeException("File storage failed", e);
        }
    }


}