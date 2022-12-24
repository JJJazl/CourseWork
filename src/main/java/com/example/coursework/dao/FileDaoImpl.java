package com.example.coursework.dao;

import com.example.coursework.domain.File;

import java.sql.*;

public class FileDaoImpl implements FileDao {
    private final DaoFactory factory = DaoFactory.getInstance();

    public FileDaoImpl() {
        String slqCreate = "CREATE TABLE IF NOT EXISTS files " +
                "(id SERIAL PRIMARY KEY, " +
                "path VARCHAR(255) NOT NULL UNIQUE, " +
                "text VARCHAR(255), " +
                "user_id INTEGER, " +
                "CONSTRAINT fk_files_users FOREIGN KEY(user_id) REFERENCES users(id))";
        try(Connection connection = factory.getConnection();
            Statement statement = connection.createStatement()) {

            statement.executeUpdate(slqCreate);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void create(String path, int userId) {
        String sql = "INSERT INTO files(path, user_id) VALUES(?,?)";

        try(Connection connection = factory.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, path);
            statement.setInt(2, userId);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("File does not created!");
        }
    }

    @Override
    public File findByFilePath(String path) {
        String sql = "SELECT * FROM files WHERE path = ?";
        File file = null;

        try(Connection connection = factory.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql))    {

            statement.setString(1, path);
            try(ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    file = new File(
                            resultSet.getLong("id"),
                            resultSet.getString("path"),
                            resultSet.getString("text"),
                            resultSet.getLong("user_id")
                    );
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return file;
    }

    @Override
    public void delete(Long id) {
        String sqlDelete = "DELETE FROM files WHERE id = ?";
        try(Connection connection = factory.getConnection();
            PreparedStatement statement = connection.prepareStatement(sqlDelete)) {
            statement.setLong(1, id);
            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
