package org.kaczucha.service;

import org.kaczucha.controller.dto.ClientRequest;
import org.kaczucha.controller.dto.ClientResponse;
import org.kaczucha.repository.entity.Account;
import org.kaczucha.repository.entity.Client;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class ClientMapper {
    public ClientResponse map(Client client) {
        final List<Long> accountsId = client.getAccountList().stream().map(Account::getId).toList();
        final ClientResponse response = ClientResponse.builder()
                .id(client.getId())
                .name(client.getName())
                .email(client.getEmail())
                .accounts(accountsId)
                .build();

        return response;
    }

    public Client map(ClientRequest client) {
        final String name = client.getName();
        final String email = client.getEmail();
        //final List<Account> accounts = Collections.singletonList(new Account(60, "LPG"));
        Client resultClient = Client.builder()
                .name(name)
                .email(email)
                //.accountList(accounts)
                .build();

        return resultClient;
    }
}
