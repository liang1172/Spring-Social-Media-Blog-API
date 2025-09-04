package com.example.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.entity.Account;
import com.example.repository.AccountRepository;

@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;

    public ResponseEntity<Account> register(Account account) {
        if (account.getUsername() == null || account.getUsername().isBlank()) {
            return ResponseEntity.status(400).build();
        }
        if (account.getPassword() == null || account.getPassword().length() < 4) {
            return ResponseEntity.status(400).build();
        }
        if (accountRepository.findByUsername(account.getUsername()).isPresent()) {
            return ResponseEntity.status(409).build();
        }
        Account newAccount = accountRepository.save(account);
        return ResponseEntity.ok(newAccount);
    }

    public ResponseEntity<Account> login(Account account) {
        Optional<Account> existing = accountRepository.findByUsernameAndPassword(account.getUsername(), account.getPassword());
        return existing.isPresent() ? ResponseEntity.ok(existing.get()) : ResponseEntity.status(401).build();
    }
}
