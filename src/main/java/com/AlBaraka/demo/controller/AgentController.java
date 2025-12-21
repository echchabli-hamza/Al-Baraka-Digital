package com.AlBaraka.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/agent")
public class AgentController {


    @GetMapping("/operations/pending")
    public ResponseEntity<?> getPendingOperations() {
        return ResponseEntity.ok("LIST PENDING OPERATIONS");
    }


    @PutMapping("/operations/{id}/approve")
    public ResponseEntity<?> approveOperation(@PathVariable Long id) {
        return ResponseEntity.ok("APPROVE OPERATION: " + id);
    }


    @PutMapping("/operations/{id}/reject")
    public ResponseEntity<?> rejectOperation(@PathVariable Long id) {
        return ResponseEntity.ok("REJECT OPERATION: " + id);
    }


    @GetMapping("/operations/{id}")
    public ResponseEntity<?> getOperationDetails(@PathVariable Long id) {
        return ResponseEntity.ok("VIEW OPERATION DETAILS: " + id);
    }
}
