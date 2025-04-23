package org.bropy.hikaru.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;

    @PrePersist
    protected void onCreate() {
        if (timestamp == null) {
            timestamp = LocalDateTime.now();
        }
    }

    // Implement UserDetails methods

    @Override
    public String getUsername() {
        return email; // or username, whichever you prefer to be the identifier
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Return authorities based on roles
        return List.of(() -> role.name());
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Implement logic as per your needs
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Implement logic as per your needs
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Implement logic as per your needs
    }

    @Override
    public boolean isEnabled() {
        return true; // Implement logic as per your needs
    }
}
