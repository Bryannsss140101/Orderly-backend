package com.project.ordearly.summary.infrastructure;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.ordearly.summary.domain.Summary;
import java.time.LocalDate;

public interface SummaryRepository extends JpaRepository<Summary, Long> {
    Optional<Summary> findByDate(LocalDate date);
}