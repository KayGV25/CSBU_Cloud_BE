package com.csbu.finance.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "Budgets")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Budget {

    @Id
    @Column(name = "id")
    private String id;

    //    private Account account; haven't had yet

    @Column(name = "period_start")
    private LocalDate periodStart;

    @Column(name = "period_end")
    private LocalDate periodEnd;

    @Column(name = "approved_amount")
    private Integer approvedAmount;

    @Column(name = "remaining_amount")
    private Integer remainingAmount;

    @Column(name = "description")
    private String description;

    @Column(name = "currency")
    private String currency;

    @Column(name = "created_at")
    private LocalDate createdAt;

    @OneToMany(mappedBy = "budget")
    private List<Transaction> transactions;

    @PrePersist
    public void prePersist() {
        if (createdAt == null) {
            createdAt = LocalDate.now();
        }
    }
}
