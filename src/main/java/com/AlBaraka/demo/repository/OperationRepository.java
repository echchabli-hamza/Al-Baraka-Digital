package com.AlBaraka.demo.repository;

import com.AlBaraka.demo.entity.Operation;
import com.AlBaraka.demo.entity.Account;
import com.AlBaraka.demo.entity.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OperationRepository extends JpaRepository<Operation, Long> {
    List<Operation> findByAccountSourceOrAccountDestination(Account source, Account destination);
    List<Operation>  findByStatus(Status Status);
}
