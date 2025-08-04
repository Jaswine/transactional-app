package com.jaswine.transactional_app.services;

import com.jaswine.transactional_app.db.entity.User;
import com.jaswine.transactional_app.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<User> findByUsername(String email) {
        return userRepository.findByUsername(email);
    }
}
