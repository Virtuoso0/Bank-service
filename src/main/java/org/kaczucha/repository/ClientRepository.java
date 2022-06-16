package org.kaczucha.repository;

import jdk.jshell.spi.ExecutionControl;
import org.kaczucha.Client;

public interface ClientRepository {
    void save(Client client);

    Client findByEmail(String email);
    void deleteByEmail(String email);
}
