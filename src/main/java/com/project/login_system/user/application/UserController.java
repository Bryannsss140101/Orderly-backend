package com.project.login_system.user.application;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.project.login_system.user.domain.UserService;
import com.project.login_system.user.dto.RegisterRequestDto;
import com.project.login_system.user.dto.UpdateEmailDto;
import com.project.login_system.user.dto.UpdatePasswordDto;
import com.project.login_system.user.dto.UpdateUsernameDto;
import com.project.login_system.user.dto.UserResponseDto;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {
    private final UserService userService;

    // Read

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getById(id));
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<UserResponseDto> findByUsername(@PathVariable String username) {
        return ResponseEntity.ok(userService.getByUsername(username));
    }

    @GetMapping
    public ResponseEntity<Page<UserResponseDto>> findAll(
            @PageableDefault(size = 10, sort = "username") Pageable pageable) {
        return ResponseEntity.ok(userService.getAll(pageable));
    }

    // Create

    @PostMapping
    public ResponseEntity<UserResponseDto> registerUser(
            @Valid @RequestBody RegisterRequestDto requestDto) {
        var user = userService.create(requestDto);
        var location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(user.getId())
                .toUri();

        return ResponseEntity.created(location).body(user);
    }

    // Update

    @PatchMapping("/{id}/username")
    public ResponseEntity<UserResponseDto> updateUsername(
            @PathVariable Long id,
            @Valid @RequestBody UpdateUsernameDto usernameDto) {
        return ResponseEntity.ok(
                userService.updateUsername(id, usernameDto));
    }

    @PatchMapping("/{id}/email")
    public ResponseEntity<UserResponseDto> updateEmail(
            @PathVariable Long id,
            @Valid @RequestBody UpdateEmailDto emailDto) {
        return ResponseEntity.ok(
                userService.updateEmail(id, emailDto));
    }

    @PatchMapping("/{id}/password")
    public ResponseEntity<UserResponseDto> updatePassword(
            @PathVariable Long id,
            @Valid @RequestBody UpdatePasswordDto passwordDto) {
        return ResponseEntity.ok(
                userService.updatePassword(id, passwordDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDto> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody RegisterRequestDto requestDto) {
        return ResponseEntity.ok(
                userService.update(id, requestDto));
    }

    // Delete

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        userService.deleteById(id);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/username/{username}")
    public ResponseEntity<Void> deleteByUsername(@PathVariable String username) {
        userService.deleteByUsername(username);

        return ResponseEntity.noContent().build();
    }
}
