package com.csbu.finance.Service;

import com.csbu.finance.Repository.TransactionRepository;
import com.csbu.finance.dto.PageResponse;
import com.csbu.finance.dto.TransactionDto;
import com.csbu.finance.model.Transaction;
import org.hibernate.TransactionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private S3Service s3Service;

    public String uploadImage(MultipartFile img, String fileName) throws IOException {
        return s3Service.upload(img, fileName);
    }
    public void createTransaction(Transaction transaction) {
        try {
            if (transactionRepository.findById(transaction.getId()).isPresent()) {
                throw new TransactionException("Transaction already exists with id: " + transaction.getId());
            }
            transactionRepository.save(transaction);
        } catch (TransactionException e) {
            throw e;
        } catch (Exception e) {
            throw new TransactionException("Error saving transaction", e);
        }
    }


    public PageResponse<TransactionDto> getTransactions(Integer page, Integer size){
        try {
            Sort sort = Sort.by("transactionDate").descending();
            Pageable pageable = PageRequest.of(page-1, size, sort);
            Page<Transaction> transactionsPage = transactionRepository.findAll(pageable);

            List<TransactionDto> transactionDtos = transactionsPage.getContent()
                    .stream()
                    .map(transaction -> new TransactionDto(
                            transaction.getId(),
                            transaction.getTransactionType(),
                            transaction.getAmount(),
                            transaction.getCurrency(),
                            transaction.getStatus(),
                            transaction.getTransactionDate(),
                            transaction.getDescription(),
                            transaction.getBudget().getId(),
                            transaction.getImage()
                    ))
                    .collect(Collectors.toList());

            return new PageResponse<TransactionDto>(
                    transactionsPage.getNumber() + 1, // currentPage (1-based index)
                    transactionsPage.getTotalPages(), // totalPages
                    transactionsPage.getSize(), // pageSize
                    (int) transactionsPage.getTotalElements(), // totalElement
                    transactionDtos // data
            );

        } catch (Exception e) {
            throw new TransactionException("Error getting transaction", e);
        }
    }

    public  Page<TransactionDto>   searchTransactions(
            String searchQuery,
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            String currency,
            String status,
            int page,
            int size
    ){
        try {
            Pageable pageable = PageRequest.of(page-1, size);

            Page<Transaction> transactionPage = transactionRepository.findAll(pageable);

            // Apply filtering logic to the transactions list
            List<TransactionDto> filteredTransactions = transactionPage.stream()
                    .filter(transaction ->
                            (searchQuery == null ||
                                    transaction.getDescription().toLowerCase().contains(searchQuery.toLowerCase()) ||
                                    transaction.getTransactionType().toLowerCase().contains(searchQuery.toLowerCase()) ||
                                    transaction.getId().toString().contains(searchQuery))
                                    && (startDate == null || !transaction.getTransactionDate().toLocalDate().isBefore(startDate))
                                    && (endDate == null || !transaction.getTransactionDate().toLocalDate().isAfter(endDate))
                                    && (currency == null || transaction.getCurrency().equals(currency))
                                    && (status == null || transaction.getStatus().equals(status))
                    )
                    .map(transaction -> new TransactionDto(
                            transaction.getId(),
                            transaction.getTransactionType(),
                            transaction.getAmount(),
                            transaction.getCurrency(),
                            transaction.getStatus(),
                            transaction.getTransactionDate(),
                            transaction.getDescription(),
                            transaction.getBudget().getId(),
                            transaction.getImage()
                    ))
                    .collect(Collectors.toList());
            return new PageImpl<>(filteredTransactions, pageable, transactionPage.getTotalElements());

        }catch (Exception e){
            throw new TransactionException("Error getting transaction", e);
        }
    }
    public void deleteTransactions(String id){
        try {
            transactionRepository.deleteById(id);
        } catch (Exception e) {
            throw new TransactionException("Error deleting transaction", e);
        }
    }

    public void updateTransactionsStatus(String id){
        try {
            transactionRepository.updateTransactionStatus(id);
        } catch (Exception e) {
            throw new TransactionException("Error updating transaction", e);
        }
    }

    public void updateTransactionsImg(String id, String url){
        try {
            transactionRepository.updateTransactionImg(id, url);
        } catch (Exception e) {
            throw new TransactionException("Error updating transaction", e);
        }
    }
    public Transaction getTransactionById(String id){
        try {
            return transactionRepository.findById(id)
                    .orElseThrow(() -> new TransactionException("Transaction not found with id: " + id));
        } catch (Exception e) {
            throw new TransactionException("Error getting transaction", e);
        }
    }

}
