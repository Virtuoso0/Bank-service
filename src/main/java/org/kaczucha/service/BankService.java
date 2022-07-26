package org.kaczucha.service;

import org.kaczucha.controller.dto.ClientRequest;
import org.kaczucha.controller.dto.ClientResponse;
import org.kaczucha.repository.ClientSpringJpaRepository;
import org.kaczucha.repository.entity.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class BankService {
    private final ClientSpringJpaRepository clientRepository;
    private final ClientMapper clientMapper;

    @Autowired
    public BankService(
            ClientSpringJpaRepository clientRepository,
            ClientMapper clientMapper
    ) {
        this.clientRepository = clientRepository;
        this.clientMapper = clientMapper;
    }

    public void save(Client client) {
        clientRepository.save(client);
    }

    public void save(ClientRequest clientRequest) {
        Client client = clientMapper.map(clientRequest);
        clientRepository.save(client);
    }

    public Client findByEmail(String email) {
        return clientRepository.findByEmail(email);
    }

    public ClientResponse findResponseByEmail(String email) {
        final Client client = findByEmail(email);
        final ClientResponse response = clientMapper.map(client);
        return response;
    }

    public void deleteByEmail(String email) {
        clientRepository.deleteByEmail(email);
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
        Client fromClient = findByEmail(fromEmail);
        Client toClient = findByEmail(toEmail);
        if (fromClient.getBalance() - amount >= 0) {
            fromClient.setBalance(fromClient.getBalance() - amount);
            toClient.setBalance(toClient.getBalance() + amount);
        } else {
            throw new NoSufficientFundsException("Not enough funds!");
        }
        clientRepository.save(fromClient);
        clientRepository.save(toClient);
    }

    public void withdraw(
            final String email,
            final double amount) {
        validateAmount(amount);
        if (Objects.isNull(email)) {
            throw new IllegalArgumentException("Email cant be null!");
        }
        final String lowerCaseEmail = email.toLowerCase();
        final Client client = findByEmail(lowerCaseEmail);
        if (amount > client.getBalance()) {
            throw new NoSufficientFundsException("Balance must be higher or equal then amount!");
        }
        final double newBalance = client.getBalance() - amount;
        client.setBalance(newBalance);
        clientRepository.save(client);
    }

    private void validateAmount(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive!");
        }
    }
}
