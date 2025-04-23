package org.bropy.hikaru.dto;

import lombok.*;
import org.bropy.hikaru.entity.Role;

import java.sql.Timestamp;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserDto {
    private Long id;
    private String email;
    private String login;
    private Timestamp timestamp;
    private Role role;
}