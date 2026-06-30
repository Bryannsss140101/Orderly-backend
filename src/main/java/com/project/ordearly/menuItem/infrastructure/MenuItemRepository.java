package com.project.ordearly.menuItem.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.ordearly.menuItem.domain.MenuItem;

public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {
}