package org.bropy.hikaru.service;

import org.bropy.hikaru.dto.UserDto;

import java.util.List;

public interface UserService {
    List<UserDto> getAllUsers();
    UserDto getUserById(Long id);
    UserDto getUserByEmail(String email);
    UserDto updateUser(Long id, UserDto userDto);
    void deleteUser(Long id);
}