package com.jaswine.transactional_app.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.LdapShaPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Configuration Class for {@link PasswordEncoder}
 */
@Configuration
public class PasswordEncoderConfig {
    /**
     * Crates bean {@link PasswordEncoder}
     *
     * @return bean {@link PasswordEncoder}
     */

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new LdapShaPasswordEncoder();
    }
}