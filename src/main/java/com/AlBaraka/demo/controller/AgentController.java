package com.AlBaraka.demo.controller;

import com.AlBaraka.demo.dto.OperationResponse;
import com.AlBaraka.demo.entity.Document;
import com.AlBaraka.demo.entity.Operation;
import com.AlBaraka.demo.security.UserInfoDetails;
import com.AlBaraka.demo.service.impl.AgentOperationService;
import com.AlBaraka.demo.service.impl.OperationService;
import io.jsonwebtoken.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AgentController {

    private final AgentOperationService agentOperationService;

    private final OperationService operationService;

    @GetMapping("/agentOauth")
    public ResponseEntity<List<OperationResponse>> getPendingOperations() {
        return ResponseEntity.ok(agentOperationService.getPendingOperations());
    }

    @PutMapping("/agent/operations/{id}/approve")
    public ResponseEntity<String> approveOperation(@PathVariable Long id) {
        agentOperationService.approveOperation(id);
        return ResponseEntity.ok("Operation approved successfully");
    }

    @PutMapping("/agent/operations/{id}/reject")
    public ResponseEntity<String> rejectOperation(@PathVariable Long id) {
        agentOperationService.rejectOperation(id);
        return ResponseEntity.ok("Operation rejected successfully");
    }

    @GetMapping("/agent/operations/{id}")
    public ResponseEntity<Resource> downloadDocument(@PathVariable Long id) throws IOException, MalformedURLException {


        Operation operation = agentOperationService.getOperationById(id);
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
                        "attachment; filename=\"" + document.getFileName() + "\"")
                .body(resource);
    }


}
