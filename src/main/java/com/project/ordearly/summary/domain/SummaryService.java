package com.project.ordearly.summary.domain;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.ordearly.exceptions.ResourceNotFoundException;
import com.project.ordearly.payment.domain.PaymentMethod;
import com.project.ordearly.summary.dto.SummaryResponseDto;
import com.project.ordearly.summary.infrastructure.SummaryRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class SummaryService {
    private final SummaryRepository summaryRepository;
    private final ModelMapper modelMapper;

    @Transactional(readOnly = true)
    public SummaryResponseDto getSummaryToday() {
        var summary = summaryRepository.findByDate(LocalDate.now())
                .orElseThrow(() -> new ResourceNotFoundException("There is no summary for today"));

        return modelMapper.map(summary, SummaryResponseDto.class);
    }

    @Transactional(readOnly = true)
    public SummaryResponseDto getSummaryByDate(LocalDate date) {
        var summary = summaryRepository.findByDate(date)
                .orElseThrow(() -> new ResourceNotFoundException("There is no summary for the date: " + date));

        return modelMapper.map(summary, SummaryResponseDto.class);
    }

    public SummaryResponseDto recalculateTotals(Long summaryId) {
        var summary = summaryRepository.findById(summaryId)
                .orElseThrow(() -> new ResourceNotFoundException("Summary not found"));

        var totalYape = BigDecimal.ZERO;
        var totalCash = BigDecimal.ZERO;

        for (var orden : summary.getOrders())
            for (var pago : orden.getPayments())
                if (pago.getMethod() == PaymentMethod.YAPE)
                    totalYape = totalYape.add(pago.getAmount());
                else
                    totalCash = totalCash.add(pago.getAmount());

        summary.setTotalYape(totalYape);
        summary.setTotalCash(totalCash);
        summary.setTotalIncome(totalYape.add(totalCash));

        return modelMapper.map(summaryRepository.save(summary), SummaryResponseDto.class);
    }
}