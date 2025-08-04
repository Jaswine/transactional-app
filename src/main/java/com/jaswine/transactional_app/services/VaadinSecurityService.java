package com.jaswine.transactional_app.services;

import com.jaswine.transactional_app.db.entity.User;
import com.vaadin.flow.spring.security.AuthenticationContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VaadinSecurityService {

    private final AuthenticationContext authenticationContext;

    public Optional<User> getAuthenticatedUser() {
        return authenticationContext.getAuthenticatedUser(User.class);
    }

    public void logout() {
        authenticationContext.logout();
    }
}
