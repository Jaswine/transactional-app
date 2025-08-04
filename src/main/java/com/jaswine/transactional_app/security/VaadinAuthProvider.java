package com.jaswine.transactional_app.security;

import com.jaswine.transactional_app.db.entity.User;
import com.jaswine.transactional_app.db.enums.UserType;
import com.jaswine.transactional_app.services.AuthService;
import com.jaswine.transactional_app.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class VaadinAuthProvider implements AuthenticationProvider {
    private final AuthService authService;
    private final UserService userService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String email = authentication.getName();
        String password = authentication.getCredentials().toString();

        System.out.println("Email: " + email);

        Optional<User> user = userService.findByEmail(email);
        if (user.isEmpty() || user.get().getType() != UserType.ADMIN) {
            throw new BadCredentialsException("User not found: " + email);
        }

        System.out.println("User: " + user.get().getEmail());

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(user.get().getType().name()));

        System.out.println("authorities: " + authorities.toString());

        if (authService.login(email, password).isEmpty()) {
            throw new BadCredentialsException("Password is incorrect: " + email);
        }

        return new UsernamePasswordAuthenticationToken(email, password, authorities);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
