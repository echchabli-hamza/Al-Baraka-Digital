package com.AlBaraka.demo.repository;


import com.AlBaraka.demo.entity.Document;
import com.AlBaraka.demo.entity.Operation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DocumentRepository extends JpaRepository<Document, Long> {
    List<Document> findByOperation(Operation operation);

    List<Document> findByOperationId(Long operationId);
}