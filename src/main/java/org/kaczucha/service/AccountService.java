package org.kaczucha.service;

import lombok.AllArgsConstructor;
import org.kaczucha.controller.dto.AccountRequest;
import org.kaczucha.controller.dto.AccountResponse;
import org.kaczucha.repository.AccountRepository;
import org.kaczucha.repository.entity.Account;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AccountService {
    private final AccountRepository repository;
    private final CurrencyService currencyService;

    public AccountResponse findById(Long id) {
        AccountResponse accountResponse = repository
                .findById(id)
                .map(account -> AccountResponse.builder()
                        .id(account.getId())
                        .balance(account.getBalance())
                        .currency(account.getCurrency())
                        .userId(account.getUserId())
                        .build())
                .orElseThrow(() -> new IllegalArgumentException("Account with id " + id + " not found!"));

        return accountResponse;
    }

    public void save(AccountRequest accountRequest) {
        Account account = Account.builder()
                .balance(accountRequest.getBalance())
                .currency(accountRequest.getCurrency())
                .userId(accountRequest.getUserId())
                .build();

        repository.save(account);
    }

    public void transfer(
            long fromAccountId,
            long toAccountId,
            double amount
    ) {
        validateAmount(amount);
        if (fromAccountId == toAccountId) {
            throw new IllegalArgumentException("fromEmail and toEmail cant be equal!");
        }

        Account fromAccount = repository.getReferenceById(fromAccountId);
        Account toAccount = repository.getReferenceById(toAccountId);

        if (fromAccount.getBalance() - amount < 0) {
            throw new NoSufficientFundsException("Not enough funds!");
        }

        if (fromAccount.getCurrency().equals(toAccount.getCurrency())) {
            fromAccount.setBalance(fromAccount.getBalance() - amount);
            toAccount.setBalance(toAccount.getBalance() + amount);
        }
        else if (fromAccount.getCurrency().equals("PLN")) {
            final String currency = toAccount.getCurrency();
            final double currencyRate = currencyService.getCurrencyRates(currency);
            fromAccount.setBalance(fromAccount.getBalance() - amount);
            toAccount.setBalance(toAccount.getBalance() + amount / currencyRate);
        }
        else if (toAccount.getCurrency().equals("PLN")) {
            final String currency = fromAccount.getCurrency();
            final double currencyRate = currencyService.getCurrencyRates(currency);
            fromAccount.setBalance(fromAccount.getBalance() - amount);
            toAccount.setBalance(toAccount.getBalance() + amount * currencyRate);
        }
        else {
            final String fromCurrency = fromAccount.getCurrency();
            final String toCurrency = toAccount.getCurrency();
            final double fromCurrencyRate = currencyService.getCurrencyRates(fromCurrency);
            final double toCurrencyRate = currencyService.getCurrencyRates(toCurrency);
            final double finalRate = fromCurrencyRate / toCurrencyRate;

            fromAccount.setBalance(fromAccount.getBalance() - amount);
            toAccount.setBalance(toAccount.getBalance() + amount * finalRate);
        }

        repository.save(fromAccount);
        repository.save(toAccount);
    }

//    public void withdraw(
//            final String email,
//            final double amount) {
//        validateAmount(amount);
//        if (Objects.isNull(email)) {
//            throw new IllegalArgumentException("Email cant be null!");
//        }
//        final String lowerCaseEmail = email.toLowerCase();
//        final Client client = clientService.findByEmail(lowerCaseEmail);
//        if (amount > client.getBalance()) {
//            throw new NoSufficientFundsException("Balance must be higher or equal then amount!");
//        }
//        final double newBalance = client.getBalance() - amount;
//        client.setBalance(newBalance);
//        clientService.save(client);
//    }

    private void validateAmount(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive!");
        }
    }
}
