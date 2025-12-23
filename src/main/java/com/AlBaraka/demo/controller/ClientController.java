package com.AlBaraka.demo.controller;

import com.AlBaraka.demo.dto.CreateOperationRequest;
import com.AlBaraka.demo.dto.OperationResponse;
import com.AlBaraka.demo.entity.enums.Status;
import com.AlBaraka.demo.security.UserInfoDetails;
import com.AlBaraka.demo.service.impl.OperationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/client")
@RequiredArgsConstructor
public class ClientController {

    private final OperationService operationService;

    @PostMapping("/operations")
    public ResponseEntity<OperationResponse> createOperation(@RequestBody CreateOperationRequest request) {

        Long userId = ((UserInfoDetails) Objects.requireNonNull(Objects.requireNonNull(SecurityContextHolder.getContext()
                .getAuthentication()).getPrincipal())).getId();

        OperationResponse response;

        if (request.getAmount() == null || request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("amount must be more than 0  ");
        }


        switch (request.getType()) {
            case DEPOSIT:
                response = operationService.createDeposit(userId, request);
                break;
            case WITHDRAWAL:
                response = operationService.createWithdrawal(userId, request);
                break;
            case TRANSFER:
                response = operationService.createTransfer(userId, request);
                break;
            default:
                return ResponseEntity.badRequest().body(null);
        }

        if (response.getStatus() == Status.PENDING) {
            response.setMessage("Operation pending: please upload a document to complete validation");
        }

        return ResponseEntity.ok(response);
    }


    @GetMapping("/operations")
    public ResponseEntity<List<OperationResponse>> getClientOperations(@RequestParam("userId") Long userId) {
        List<OperationResponse> operations = operationService.getClientOperations(userId);
        return ResponseEntity.ok(operations);
    }

    @GetMapping("/operations/{id}")
    public ResponseEntity<OperationResponse> getOperationById(
            @PathVariable Long id,
            @RequestParam("userId") Long userId
    ) {
        OperationResponse operation = operationService.getOperationById(id, userId);
        return ResponseEntity.ok(operation);
    }

    @PostMapping("/operations/{id}/document")
    public ResponseEntity<?> uploadDocument(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file
    ) {
        Long userId = ((UserInfoDetails) Objects.requireNonNull(Objects.requireNonNull(SecurityContextHolder
                        .getContext()
                        .getAuthentication())
                .getPrincipal()))
                .getId();

        operationService.uploadDocument(id, userId, file);

        return ResponseEntity.ok("Document uploaded successfully");
    }

}
