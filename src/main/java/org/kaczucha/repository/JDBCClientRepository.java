package org.kaczucha.repository;

import org.kaczucha.repository.entity.Client;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class JDBCClientRepository implements ClientRepository {

    public static final String USER = "postgres";
    public static final String PASSWORD = "qwerty";
    public static final String JDBC_URL = "jdbc:postgresql://127.0.0.1:5432/test";

    @Override
    public void save(Client client) {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USER, PASSWORD);) {
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
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USER, PASSWORD);) {
            final PreparedStatement preparedStatement = connection.prepareStatement("SELECT first_name, mail FROM users WHERE mail = ?");
            preparedStatement.setString(1, email);
            final ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next()) {
                String name = resultSet.getString("first_name");
                String mail = resultSet.getString("mail");
                return new Client(name, mail, 0);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public void deleteByEmail(String email) {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USER, PASSWORD);) {
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM users WHERE mail = ?");
            preparedStatement.setString(1, email);
            preparedStatement.execute();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
