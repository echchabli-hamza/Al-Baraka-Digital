package com.AlBaraka.demo.controller;

import com.AlBaraka.demo.dto.CreateOperationRequest;
import com.AlBaraka.demo.dto.OperationResponse;
import com.AlBaraka.demo.entity.Document;
import com.AlBaraka.demo.entity.Operation;
import com.AlBaraka.demo.entity.enums.Status;
import com.AlBaraka.demo.security.UserInfoDetails;
import com.AlBaraka.demo.service.impl.OperationService;
import io.jsonwebtoken.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import org.springframework.core.io.Resource;


import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
    public ResponseEntity<List<OperationResponse>> getClientOperations() {
        Long userId = ((UserInfoDetails) Objects.requireNonNull(Objects.requireNonNull(SecurityContextHolder
                        .getContext()
                        .getAuthentication())
                .getPrincipal()))
                .getId();
        List<OperationResponse> operations = operationService.getClientOperations(userId);
        return ResponseEntity.ok(operations);
    }

    @GetMapping("/operations/{id}")
    public ResponseEntity<Resource> downloadDocument(@PathVariable Long id) throws IOException, MalformedURLException {
        Long userId = ((UserInfoDetails) Objects.requireNonNull(
                SecurityContextHolder.getContext().getAuthentication().getPrincipal()
        )).getId();

        Operation operation = operationService.getOperationById(id, userId);
        Document document = operation.getDocument();

        if (document == null) {
            return ResponseEntity.notFound().build();
        }

        Path filePath = Paths.get(document.getStoragePath());
        if (!Files.exists(filePath)) {
            return ResponseEntity.notFound().build();
        }

        Resource resource = new UrlResource(filePath.toUri());

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF) // tells browser it's a PDF
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + document.getFileName() + "\"") // forces download
                .body(resource);
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
