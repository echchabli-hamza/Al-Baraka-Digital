package com.AlBaraka.demo.repository;

import com.AlBaraka.demo.entity.Account;
import com.AlBaraka.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

    List<Account> findByOwner(User owner);

    Account findByOwnerId(Long userId) ;

    List<Account> findAllByOwner(User owner);

    boolean existsByAccountNumber(String accountNumber);


  Optional<Account> findByAccountNumber(String number) ;


}
