package org.kaczucha.service;

import lombok.RequiredArgsConstructor;
import org.kaczucha.controller.dto.TransactionRequest;
import org.kaczucha.repository.TransactionRepository;
import org.kaczucha.repository.entity.TransactionEntity;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionRepository repository;
    private final AccountService accountService;

    public void createTransaction(TransactionRequest transactionRequest) {
        final double amount = transactionRequest.getAmount();
        final String currency = "PLN";
        final long fromAccountId = transactionRequest.getFromAccountId();
        final long toAccountId = transactionRequest.getToAccountId();

        accountService.transfer(fromAccountId, toAccountId, amount);

        repository.save(
                TransactionEntity.builder()
                        .amount(amount)
                        .currency(currency)
                        .transactionDate(OffsetDateTime.now())
                        .fromAccountId(fromAccountId)
                        .toAccountId(toAccountId)
                        .build()
        );
    }
}
