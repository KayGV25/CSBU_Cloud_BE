package com.csbu.finance.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class AddTransactionRequest {
    @NotNull
    private String id;
    @NotNull
    private String transactionType;

    @NotNull
    private Integer amount;

    @NotNull
    private String currency;

    @NotNull
    private String description;
    @NotNull
    private  String budgetId;
}
