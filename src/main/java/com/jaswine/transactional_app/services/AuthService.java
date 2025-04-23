package com.jaswine.transactional_app.services;

import com.jaswine.transactional_app.db.entity.Account;
import com.jaswine.transactional_app.db.entity.User;
import com.jaswine.transactional_app.db.enums.UserType;
import com.jaswine.transactional_app.repositories.AccountRepository;
import com.jaswine.transactional_app.repositories.UserRepository;
import com.jaswine.transactional_app.utils.AccountUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    public void register(String username, String email, String password) {
        if (findByEmail(email).isPresent()) {
            throw new RuntimeException("Email already in use");
        }
        if (findByUsername(username).isPresent()) {
            throw new RuntimeException("Username already taken");
        }

        User user = User.builder()
                .username(username)
                .email(email)
                .password(passwordEncoder.encode(password))
                .type(UserType.SIMPLE)
                .isActive(true)
                .build();

        Account account = Account.builder()
                .address(AccountUtils.generateAddress())
                .amount((float) 0)
                .isActive(true)
                .user(user)
                .build();

        userRepository.save(user);
        accountRepository.save(account);
    }

    public Optional<User> login(String email, String rawPassword) {
        Optional<User> user = Optional.ofNullable(findByEmail(email)
                .orElseThrow(() -> new BadCredentialsException("Invalid email or password")));

        if (user.isEmpty() || !passwordEncoder.matches(rawPassword, user.get().getPassword())) {
            throw new BadCredentialsException("Invalid email or password");
        }

        return user;
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<User> findByUsername(String email) {
        return userRepository.findByUsername(email);
    }
}
