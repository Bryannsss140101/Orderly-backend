package com.project.ordearly.menuItem.infrastructure;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.ordearly.menuItem.domain.MenuItem;

public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {
    List<MenuItem> findByAvailableTrue();
}