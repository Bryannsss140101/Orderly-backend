package com.project.ordearly.user.domain;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.ZonedDateTime;
import java.util.Collection;

import org.junit.Test;
import org.springframework.security.core.GrantedAuthority;

public class UserTest {
    @Test
    public void shouldSetCreatedAt_whenCreatedAtIsNull() {
        var user = new User();
        user.setCreatedAt(null);

        user.onCreate();

        assertNotNull(user.getCreatedAt());
    }

    @Test
    public void shouldNotOverrideCreatedAt_whenCreatedAtAlreadyExists() {
        var now = ZonedDateTime.now();

        var user = new User();
        user.setCreatedAt(now);

        user.onCreate();

        assertEquals(now, user.getCreatedAt());
    }

    @Test
    public void shouldReturnRoleAuthority_whenRoleIsAdmin() {
        var user = User.builder()
                .role(Role.ADMIN)
                .build();

        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();

        assertEquals(1, authorities.size());
        assertTrue(authorities.stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")));
    }
}