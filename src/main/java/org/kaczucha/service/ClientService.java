package org.kaczucha.service;

import org.kaczucha.controller.dto.ClientRequest;
import org.kaczucha.controller.dto.ClientResponse;
import org.kaczucha.repository.ClientRepository;
import org.kaczucha.repository.entity.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClientService {
    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;

    @Autowired
    public ClientService(
            ClientRepository clientRepository,
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
}
