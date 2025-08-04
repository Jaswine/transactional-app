package com.jaswine.transactional_app.services;

import com.jaswine.transactional_app.db.entity.Account;
import com.jaswine.transactional_app.repositories.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;

    public Page<Account> findAllAccounts(int page, int limit,
                                         String searchingText, Float amountMinValue, Float amountMaxValue) {
        return accountRepository.findActiveAccountsByUserFilter(PageRequest.of(page, limit),
                searchingText, amountMinValue, amountMaxValue);
    }

    public Optional<Account> findById(long id) {
        return accountRepository.findById(id);
    }

    public Optional<Account> findByUserEmail(String email) {
        return accountRepository.findByUserEmail(email);
    }

    public void save(Account account) {
        accountRepository.save(account);
    }

}
