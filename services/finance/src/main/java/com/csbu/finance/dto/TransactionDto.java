package com.csbu.finance.dto;

import java.time.LocalDateTime;

public record TransactionDto(
        String id,
        String transactionType,
        Integer amount,
        String currency,
        Boolean status,
        LocalDateTime transactionDate,
        String description,
        String budget_id,
        String image
) {
}
