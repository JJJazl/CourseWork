package com.example.coursework.dao;

import com.example.coursework.domain.User;

import java.sql.*;
import java.util.Optional;

public class UserDaoImpl implements UserDao {
    private final DaoFactory factory = DaoFactory.getInstance();

    public UserDaoImpl() {
        String slqCreate = "CREATE TABLE IF NOT EXISTS users " +
                "(id SERIAL PRIMARY KEY, " +
                "username VARCHAR(255) NOT NULL UNIQUE, " +
                "password VARCHAR(255) NOT NULL)";
        try(Connection connection = factory.getConnection();
            Statement statement = connection.createStatement()) {

            statement.executeUpdate(slqCreate);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int create(String username, String password) {
        String sql = "INSERT INTO users(username, password) VALUES(?,?)";

        int id = -1;

        try(Connection connection = factory.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql,
                    Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, username);
            statement.setString(2, password);
            statement.executeUpdate();
            try(ResultSet resultSet = statement.getGeneratedKeys()) {
                resultSet.next();
                id = resultSet.getInt("id");
            }
        } catch (SQLException e) {
            System.out.println("User with the same username already exists");
        }
        return id;
    }

    @Override
    public boolean existByUsername(String username) {
        String sql = "SELECT username FROM users WHERE username = ?";
        String result = null;

        try(Connection connection = factory.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql))    {

            statement.setString(1, username);
            try(ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    result = resultSet.getString("username");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result != null;
    }

    @Override
    public Optional<User> findByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        User user = null;

        try(Connection connection = factory.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql))    {

            statement.setString(1, username);
            try(ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    user = new User(
                            resultSet.getLong("id"),
                            resultSet.getString("username"),
                            resultSet.getString("password")
                    );
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.ofNullable(user);
    }

    @Override
    public void delete(Long id) {
        String sqlDelete = "DELETE FROM users WHERE id = ?";
        try(Connection connection = factory.getConnection();
            PreparedStatement statement = connection.prepareStatement(sqlDelete)) {
            statement.setLong(1, id);
            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
