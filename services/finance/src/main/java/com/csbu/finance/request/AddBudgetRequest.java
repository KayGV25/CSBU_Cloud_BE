package com.csbu.finance.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@Getter
@Setter
public class AddBudgetRequest {
    @NotNull
    private String id;
    @NotNull
    private String description;
    @NotNull
    private LocalDate periodStart;
    @NotNull
    private LocalDate periodEnd;
    @NotNull
    private Integer budgetAmount;
    @NotNull
    private String currency;
}
