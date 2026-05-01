package com.csbu.finance.Repository;

import com.csbu.finance.model.Budget;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BudgetRepository extends JpaRepository<Budget, String> {
    @Modifying
    @Transactional
    @Query("UPDATE Budget a SET a.remainingAmount = :amount WHERE a.id = :id")
    void updateBudgetAmount(@Param("id") String id, @Param("amount") Integer amount);
}
