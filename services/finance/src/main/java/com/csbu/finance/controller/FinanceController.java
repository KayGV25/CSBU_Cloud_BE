package com.csbu.finance.controller;

import com.csbu.finance.Service.BudgetService;
import com.csbu.finance.Service.TransactionService;
import com.csbu.finance.dto.BudgetDto;
import com.csbu.finance.dto.BudgetIdAndCurrencyDto;
import com.csbu.finance.dto.PageResponse;
import com.csbu.finance.dto.TransactionDto;
import com.csbu.finance.model.Budget;
import com.csbu.finance.model.Transaction;
import com.csbu.finance.request.AddBudgetRequest;
import com.csbu.finance.request.AddTransactionRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1/finance")
@RequiredArgsConstructor
public class FinanceController {
    @Autowired
    private TransactionService transactionService;

    @Autowired
    private BudgetService budgetService;

    @PostMapping("/transactions")
    public ResponseEntity<String> createTransaction(@RequestBody @Valid AddTransactionRequest request)
    {
        Budget budget = budgetService.getBudgetById(request.getBudgetId());
        Transaction transaction = new Transaction();
        transaction.setId(request.getId());
        transaction.setTransactionType(request.getTransactionType());
        transaction.setAmount(request.getAmount());
        transaction.setCurrency(request.getCurrency());
        transaction.setStatus(false);
        transaction.setDescription(request.getDescription());
        transaction.setBudget(budget);
        transaction.setImage(null);
        transactionService.createTransaction(transaction);
        return ResponseEntity.status(HttpStatus.CREATED).body("Transaction added successfully");
    }
    @GetMapping("/transactions")
    public ResponseEntity<PageResponse<TransactionDto>> handleTransactions(
            @RequestParam(value = "searchQuery", required = false) String searchQuery,
            @RequestParam(value = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(value = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(value = "currency", required = false) String currency,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(value = "size", required = false, defaultValue = "10") Integer size
    ) {
        if (searchQuery != null || startDate != null || endDate != null || currency != null || status != null) {
            Page<TransactionDto> transactions = transactionService.searchTransactions(searchQuery, startDate, endDate, currency, status, page, size);
            PageResponse<TransactionDto> pageResponse = new PageResponse<>(
                    transactions.getNumber() + 1,
                    transactions.getTotalPages(),
                    transactions.getSize(),
                    (int) transactions.getTotalElements(),
                    transactions.getContent()
            );
            return ResponseEntity.ok(pageResponse);
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(transactionService.getTransactions(page, size));
        }
    }



    @DeleteMapping("/transactions/{id}")
    public ResponseEntity<String> deleteTransactions(@PathVariable(name="id") String id){
        transactionService.deleteTransactions(id);
        return  ResponseEntity.status(HttpStatus.OK).body(String.format("Transaction %s has been deleted!",id));
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PutMapping("/transactions/{id}/status")
    public ResponseEntity<String> updateTransactionsStatus(@PathVariable(name = "id") String id,@RequestParam("file") MultipartFile file) throws IOException{
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("File is empty!");
        }
        if (!Objects.requireNonNull(file.getContentType()).startsWith("image/")) {
            return ResponseEntity.badRequest().body("Invalid file type! Only image files are allowed.");
        }
        String downloadUrl = transactionService.uploadImage(file, id);
        transactionService.updateTransactionsImg(id, downloadUrl);
        try {
            Transaction transaction = transactionService.getTransactionById(id);
            Budget budget = transaction.getBudget();

            String budgetId = budget.getId();
            Integer budgetRemaining = budget.getRemainingAmount();
            Integer transactionAmount = transaction.getAmount();

            if (transactionAmount > budgetRemaining) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(String.format("Transaction amount of %d exceeds remaining budget of %d!", transactionAmount, budgetRemaining));
            }

            Integer remaining = budgetRemaining - transactionAmount;

            transactionService.updateTransactionsStatus(id);
            budgetService.updateBudgetAmount(budgetId, remaining);

            return ResponseEntity.status(HttpStatus.OK)
                    .body(String.format("Transaction %s status has been updated and the remaining budget is %d!", id, remaining));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error processing transaction update: " + e.getMessage());
        }
    }

    @PostMapping("/budgets")
    public ResponseEntity<String> createBudget(@RequestBody @Valid AddBudgetRequest request)
    {
        Budget existingBudget = budgetService.getBudgetById(request.getId());
        if (existingBudget != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Budget already exists with id: " + request.getId());
        }
        
        Budget budget = new Budget();
        budget.setId(request.getId());
        budget.setPeriodStart(request.getPeriodStart());
        budget.setDescription(request.getDescription());
        budget.setPeriodEnd(request.getPeriodEnd());
        budget.setCurrency(request.getCurrency());
        budget.setApprovedAmount(request.getBudgetAmount());
        budget.setRemainingAmount(request.getBudgetAmount());

        budgetService.createBudget(budget);
        return ResponseEntity.status(HttpStatus.CREATED).body("Budget added successfully");
    }

    @GetMapping("/budgets")
    public ResponseEntity<PageResponse<BudgetDto>> getBudgets(
            @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(value = "size", required = false, defaultValue = "10") Integer size,
            @RequestParam(value = "searchQuery", required = false) String searchQuery,
            @RequestParam(value = "year", required = false) String year
    ){
        if (searchQuery != null || year != null) {
            Page<BudgetDto> transactions = budgetService.searchBudgets(searchQuery, year , page, size);
            PageResponse<BudgetDto> pageResponse = new PageResponse<>(
                    transactions.getNumber() + 1,
                    transactions.getTotalPages(),
                    transactions.getSize(),
                    (int) transactions.getTotalElements(),
                    transactions.getContent()
            );
            return ResponseEntity.ok(pageResponse);
        } else {
            return  ResponseEntity.status(HttpStatus.OK).body(budgetService.getBudgets(page, size));
        }
    }

    @GetMapping("/budgets/id-currency")
    public ResponseEntity<List<BudgetIdAndCurrencyDto>> getAllBudgetsIdAndCurrency(
    ){
        return  ResponseEntity.status(HttpStatus.OK).body(budgetService.getAllBudgetsIdAndCurrency());
    }



    @DeleteMapping("/budgets/{id}")
    public ResponseEntity<String> deleteBudgets(@PathVariable(name="id") String id){
        budgetService.deleteBudget(id);
        return  ResponseEntity.status(HttpStatus.OK).body(String.format("Transaction %s has been deleted!",id));
    }


}
