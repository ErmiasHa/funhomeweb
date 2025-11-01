package com.funhomebase.funhomeweb.config;

import com.funhomebase.funhomeweb.model.User;
import com.funhomebase.funhomeweb.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private final UserRepository userRepository;

    public SecurityConfig(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // 🔹 Här hämtar vi användare från MongoDB
    @Bean
    public UserDetailsService userDetailsService() {
        return email -> userRepository.findByEmail(email)
                .map(u -> org.springframework.security.core.userdetails.User
                        .withUsername(u.getEmail())
                        .password(u.getPassword())
                        .roles(u.getRole())
                        .build())
                .orElseThrow(() -> new UsernameNotFoundException("Användare ej hittad: " + email));
    }

    // 🔹 Kopplar ihop UserDetailsService + PasswordEncoder
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService());
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    // 🔹 Viktigt: Detta ger en riktig AuthenticationManager
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/index.html", "/login.html", "/dashboard.html",
                                "/css/**", "/js/**", "/images/**", "/api/auth/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/products/**").hasAnyRole("ADMIN", "CUSTOMER")
                        .requestMatchers("/api/products/**").hasRole("ADMIN")
                        .requestMatchers("/api/dashboard/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .authenticationProvider(authenticationProvider())
                .httpBasic(httpBasic -> httpBasic.disable())
                .formLogin(form -> form.disable());

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
