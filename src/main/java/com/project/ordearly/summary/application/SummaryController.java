package com.project.ordearly.summary.application;

import java.time.LocalDate;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.ordearly.summary.domain.SummaryService;
import com.project.ordearly.summary.dto.SummaryResponseDto;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/summaries")
@PreAuthorize("isAuthenticated()")
public class SummaryController {
    private final SummaryService summaryService;

    @GetMapping("/today")
    public ResponseEntity<SummaryResponseDto> getSummaryToday() {
        return ResponseEntity.ok(summaryService.getSummaryToday());
    }

    @GetMapping
    public ResponseEntity<SummaryResponseDto> getSummaryByDate(
            @RequestParam LocalDate date) {

        return ResponseEntity.ok(summaryService.getSummaryByDate(date));
    }

    @PatchMapping("/{id}/recalculate")
    public ResponseEntity<SummaryResponseDto> recalculateTotals(@PathVariable Long id) {
        return ResponseEntity.ok(summaryService.recalculateTotals(id));
    }
}