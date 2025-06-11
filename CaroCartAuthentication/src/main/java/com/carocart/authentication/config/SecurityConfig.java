package com.carocart.authentication.config;

import com.carocart.authentication.service.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .cors(Customizer.withDefaults())
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                // Permit public endpoints
                .requestMatchers(
                    "/admins/signup",
                    "/admins/login",
                    "/users/login",
                    "/users/signup",
                    "/vendors/signup",
                    "/vendors/login"
                    
                ).permitAll()

                // Admin-specific routes
                .requestMatchers("/admins/me").hasRole("ADMIN")
                .requestMatchers("/users/admin/users/all").hasRole("ADMIN")
                .requestMatchers("/users/admin/users/{id}").hasRole("ADMIN")
                
                .requestMatchers(
                    "/admins/profile/upload-image",
                    "/admins/profile/image",
                    "/admins/profile",
                    "/admins/me"
                ).hasRole("ADMIN")


                // Authenticated user routes
                .requestMatchers(
                    "/users/profile/upload-image",
                    "/users/profile/image",
                    "/users/profile",
                    "/users/me"
                ).authenticated()

                // Everything else
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
            .sessionManagement(session -> session.disable())
            .httpBasic(httpBasic -> httpBasic.disable())
            .formLogin(form -> form.disable());

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}