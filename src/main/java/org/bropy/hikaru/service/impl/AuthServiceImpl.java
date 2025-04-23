package org.bropy.hikaru.service.impl;

import lombok.RequiredArgsConstructor;
import org.bropy.hikaru.dto.AuthRequest;
import org.bropy.hikaru.dto.AuthResponse;
import org.bropy.hikaru.dto.UserRegistrationDto;
import org.bropy.hikaru.entity.Role;
import org.bropy.hikaru.entity.User;
import org.bropy.hikaru.exception.EmailAlreadyExistsException;
import org.bropy.hikaru.exception.LoginAlreadyExistsException;
import org.bropy.hikaru.repository.UserRepository;
import org.bropy.hikaru.security.JwtService;
import org.bropy.hikaru.service.AuthService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;

    public AuthServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           JwtService jwtService,
                           AuthenticationManager authenticationManager,
                           UserDetailsService userDetailsService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
    }
    public AuthResponse register(UserRegistrationDto request) {
        User user = User.builder()
                .email(request.getEmail())
                .username(request.getLogin())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.valueOf("USER"))
                .timestamp(Timestamp.from(Instant.now()).toLocalDateTime()) // Add timestamp
                .build();

        userRepository.save(user);

        // Correctly load UserDetails through your UserDetailsService
        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
        String jwtToken = jwtService.generateToken(userDetails);

        return AuthResponse.builder()
                .token(jwtToken)
                .email(user.getEmail())
                .login(user.getUsername())
                .role(String.valueOf(user.getRole()))
                .build();
    }
    public AuthResponse authenticate(AuthRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow();

        // Correctly load UserDetails through your UserDetailsService
        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
        String jwtToken = jwtService.generateToken(userDetails);

        return AuthResponse.builder()
                .token(jwtToken)
                .email(user.getEmail())
                .login(user.getUsername())
                .role(String.valueOf(user.getRole()))
                .build();
    }
}