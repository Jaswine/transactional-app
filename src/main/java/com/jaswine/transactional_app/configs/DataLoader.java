package com.jaswine.transactional_app.configs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jaswine.transactional_app.db.dto.UserSeedDto;
import com.jaswine.transactional_app.db.entity.Account;
import com.jaswine.transactional_app.db.entity.User;
import com.jaswine.transactional_app.db.enums.UserType;
import com.jaswine.transactional_app.repositories.AccountRepository;
import com.jaswine.transactional_app.repositories.UserRepository;
import com.jaswine.transactional_app.utils.AccountUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

@Component
public class DataLoader implements CommandLineRunner {
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final ObjectMapper objectMapper;

    public DataLoader(UserRepository userRepository,
                      AccountRepository accountRepository,
                      PasswordEncoder passwordEncoder,
                      ObjectMapper objectMapper) {
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
        this.objectMapper = objectMapper;
    }

    @Override
    public void run(String... args) throws Exception {
        // Load initial data into the database
        if (userRepository.count() == 0) {
//            Resource resource = resourceLoader.getResource(seedProperties.getUsersFile());
//            InputStream is = resource.getInputStream();
            InputStream is = getClass().getClassLoader().getResourceAsStream("data/initial-users.json");
            if (is == null) throw new FileNotFoundException("users.json not found in resources/data");

            List<UserSeedDto> users = Arrays.asList(objectMapper.readValue(is, UserSeedDto[].class));

            for (UserSeedDto dto : users) {
                User user = User.builder()
                        .username(dto.getUsername())
                        .email(dto.getEmail())
                        .password(passwordEncoder.encode(dto.getPassword()))
                        .type(UserType.valueOf(dto.getUserType()))
                        .isActive(dto.getActive())
                        .build();

                Account account = Account.builder()
                        .address(AccountUtils.generateAddress())
                        .amount(0f)
                        .user(user)
                        .isActive(true)
                        .build();

                userRepository.save(user);
                accountRepository.save(account);
            }
        }
    }
}
