package org.bropy.hikaru.service;

import org.bropy.hikaru.dto.AuthRequest;
import org.bropy.hikaru.dto.AuthResponse;
import org.bropy.hikaru.dto.UserRegistrationDto;

public interface AuthService {
    AuthResponse register(UserRegistrationDto registrationDto);
    AuthResponse authenticate(AuthRequest authRequest);
}