package com.project.ordearly.menuItem.application;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.ordearly.menuItem.domain.MenuItemService;
import com.project.ordearly.menuItem.dto.MenuItemRequestDto;
import com.project.ordearly.menuItem.dto.MenuItemResponseDto;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/menu-items")
@PreAuthorize("isAuthenticated()")
public class MenuItemController {

    private final MenuItemService menuItemService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MenuItemResponseDto> createProduct(
            @Valid @RequestBody MenuItemRequestDto requestDto) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(menuItemService.createProduct(requestDto));
    }

    @GetMapping("/available")
    public ResponseEntity<List<MenuItemResponseDto>> listAvailable() {
        return ResponseEntity.ok(menuItemService.listAvailable());
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<MenuItemResponseDto>> listAll() {
        return ResponseEntity.ok(menuItemService.listAll());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MenuItemResponseDto> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody MenuItemRequestDto requestDto) {

        return ResponseEntity.ok(menuItemService.updateProduct(id, requestDto));
    }

    @PatchMapping("/{id}/toggle")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MenuItemResponseDto> toggleAvailability(@PathVariable Long id) {
        return ResponseEntity.ok(menuItemService.toggleAvailability(id));
    }
}
