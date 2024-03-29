package org.example.security;

// imports

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    private final JwtConverter converter;

    public SecurityConfig(JwtConverter converter) {
        this.converter = converter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationConfiguration authConfig) throws Exception {

        http.csrf().disable();

        http.cors();

        http.authorizeRequests()
                .antMatchers(HttpMethod.POST, "/api/users/authenticate").permitAll()
                .antMatchers(HttpMethod.POST, "/api/users").hasAnyAuthority("ADMIN")
                .antMatchers(HttpMethod.PUT, "/api/users/account_setup").permitAll()
                .antMatchers(HttpMethod.GET, "/api/users/applicants").hasAnyAuthority("ADMIN")
                .antMatchers(HttpMethod.POST, "/api/users/send_setup_emails/*").hasAnyAuthority("ADMIN")
                .antMatchers(HttpMethod.GET, "/api/submissions/all").hasAnyAuthority("ADMIN")
                .antMatchers(HttpMethod.GET, "/api/submissions/mine").hasAnyAuthority("APPLICANT")
                .antMatchers(HttpMethod.GET, "/api/submissions/*").hasAnyAuthority("ADMIN")
                .antMatchers(HttpMethod.PUT, "/api/submissions/mark_graded").hasAnyAuthority("ADMIN")
                .antMatchers(HttpMethod.POST, "/api/submissions").hasAnyAuthority("APPLICANT")
                .antMatchers(HttpMethod.GET, "/api/submissions/zip_files/*").hasAnyAuthority("ADMIN")
                .antMatchers(HttpMethod.PUT, "/api/submissions/bulk_archive/*").hasAnyAuthority("ADMIN")
                .antMatchers(HttpMethod.GET, "/api/test_case_outcomes/*").hasAnyAuthority("ADMIN")
                .antMatchers(HttpMethod.POST, "/api/test_case_outcomes").hasAnyAuthority("ADMIN")
                .antMatchers(HttpMethod.PUT, "/api/test_case_outcomes").hasAnyAuthority("ADMIN")
                .antMatchers(HttpMethod.GET, "/api/grading_batches").hasAnyAuthority("ADMIN")
                .antMatchers(HttpMethod.GET, "/api/grading_batches/*").hasAnyAuthority("ADMIN")
                .antMatchers(HttpMethod.POST, "/api/grading_batches").hasAnyAuthority("ADMIN")

                .antMatchers("/**").denyAll()
                .and()
                .addFilter(new JwtRequestFilter(authenticationManager(authConfig), converter))
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        return http.build();
    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
