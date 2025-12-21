package com.AlBaraka.demo.controller;



import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/client")
public class ClientController {

    @PostMapping("/operations")
    public ResponseEntity<?> createOperation(@RequestBody Object request) {
        return ResponseEntity.ok("CREATE OPERATION (deposit / withdrawal / transfer)");
    }

    @GetMapping("/operations")
    public ResponseEntity<?> getClientOperations() {
        return ResponseEntity.ok("LIST CLIENT OPERATIONS");
    }

    @GetMapping("/operations/{id}")
    public ResponseEntity<?> getOperationById(@PathVariable Long id) {
        return ResponseEntity.ok("GET OPERATION DETAILS: " + id);
    }

    @PostMapping("/operations/{id}/document")
    public ResponseEntity<?> uploadDocument(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file
    ) {
        return ResponseEntity.ok("UPLOAD DOCUMENT FOR OPERATION: " + id);
    }
}

