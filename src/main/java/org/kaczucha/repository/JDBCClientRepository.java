package org.kaczucha.repository;

import org.kaczucha.repository.annotation.JDBCRepository;
import org.kaczucha.repository.entity.Client;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@Repository
@JDBCRepository
public class JDBCClientRepository implements ClientRepository {

    public final String user;
    public final String password;
    public final String jdbcUrl;

    public JDBCClientRepository(
            @Value("${jdbc.user}")String user,
            @Value("${jdbc.password}")String password,
            @Value("${jdbc.url}")String jdbcUrl)
    {
        this.user = user;
        this.password = password;
        this.jdbcUrl = jdbcUrl;
    }

    @Override
    public void save(Client client) {
        try (Connection connection = DriverManager.getConnection(jdbcUrl, user, password);) {
            final String name = client.getName();
            final String mail = client.getEmail();

            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO users(first_name, mail) VALUES(?,?)");
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, mail);
            preparedStatement.execute();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public Client findByEmail(String email) {
        try (Connection connection = DriverManager.getConnection(jdbcUrl, user, password);) {
            final PreparedStatement preparedStatement = connection.prepareStatement("SELECT first_name, mail FROM users WHERE mail = ?");
            preparedStatement.setString(1, email);
            final ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next()) {
                String name = resultSet.getString("first_name");
                String mail = resultSet.getString("mail");
                return new Client(name, mail, null);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public void deleteByEmail(String email) {
        try (Connection connection = DriverManager.getConnection(jdbcUrl, user, password);) {
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM users WHERE mail = ?");
            preparedStatement.setString(1, email);
            preparedStatement.execute();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
