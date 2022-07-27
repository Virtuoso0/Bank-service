package org.kaczucha.service;

import org.kaczucha.repository.entity.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class BankService {
    private ClientService clientService;

    @Autowired
    public BankService(ClientService clientService) {
        this.clientService = clientService;
    }

    public void transfer(
            String fromEmail,
            String toEmail,
            double amount
    ) {
        validateAmount(amount);
        if (fromEmail.equals(toEmail)) {
            throw new IllegalArgumentException("fromEmail and toEmail cant be equal!");
        }
        Client fromClient = clientService.findByEmail(fromEmail);
        Client toClient = clientService.findByEmail(toEmail);
        if (fromClient.getBalance() - amount >= 0) {
            fromClient.setBalance(fromClient.getBalance() - amount);
            toClient.setBalance(toClient.getBalance() + amount);
        } else {
            throw new NoSufficientFundsException("Not enough funds!");
        }
        clientService.save(fromClient);
        clientService.save(toClient);
    }

    public void withdraw(
            final String email,
            final double amount) {
        validateAmount(amount);
        if (Objects.isNull(email)) {
            throw new IllegalArgumentException("Email cant be null!");
        }
        final String lowerCaseEmail = email.toLowerCase();
        final Client client = clientService.findByEmail(lowerCaseEmail);
        if (amount > client.getBalance()) {
            throw new NoSufficientFundsException("Balance must be higher or equal then amount!");
        }
        final double newBalance = client.getBalance() - amount;
        client.setBalance(newBalance);
        clientService.save(client);
    }

    private void validateAmount(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive!");
        }
    }
}
