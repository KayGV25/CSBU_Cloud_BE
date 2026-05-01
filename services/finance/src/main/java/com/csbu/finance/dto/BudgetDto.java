package com.csbu.finance.dto;

import java.time.LocalDate;

public record BudgetDto(
        String id,
        String description,
        LocalDate periodStart,
        LocalDate periodEnd,
        Integer approvedAmount,
        Integer remainingAmount,
        String currency,
        LocalDate createdAt
) {

}
