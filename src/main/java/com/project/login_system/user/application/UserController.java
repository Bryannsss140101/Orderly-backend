package com.project.login_system.user.application;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.project.login_system.user.domain.UserService;
import com.project.login_system.user.dto.UserResponseDto;
import com.project.login_system.user.dto.UserUpdateDto;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {
    private final UserService userService;

    // Read
    @GetMapping("/{id}")
    @PreAuthorize("@securityUtils.isCurrentUserOrAdmin(#id)")
    public ResponseEntity<UserResponseDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getById(id));
    }

    @GetMapping("/username/{username}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<UserResponseDto> findByUsername(@PathVariable String username) {
        return ResponseEntity.ok(userService.getByUsername(username));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<UserResponseDto>> findAll(
            @PageableDefault(size = 10, sort = "username") Pageable pageable) {
        return ResponseEntity.ok(userService.getAll(pageable));
    }

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserResponseDto> getCurrentUser() {
        return ResponseEntity.ok(userService.getCurrentUser());
    }

    // Update
    @PutMapping("/{id}")
    @PreAuthorize("@securityUtils.isCurrentUserOrAdmin(#id)")
    public ResponseEntity<UserResponseDto> update(
            @PathVariable Long id,
            @Valid @RequestBody UserUpdateDto dto) {
        return ResponseEntity.ok(userService.update(id, dto));
    }

    // Delete
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}