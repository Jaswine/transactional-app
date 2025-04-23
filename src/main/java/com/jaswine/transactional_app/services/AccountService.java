package com.jaswine.transactional_app.services;

import com.jaswine.transactional_app.db.entity.Account;
import com.jaswine.transactional_app.repositories.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;

    public List<Account> findAllAccounts(String username, String email) {
        return accountRepository.findActiveAccountsByUserFilter(username, email);
    }
}
