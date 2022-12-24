package com.example.coursework.dao;

import com.example.coursework.domain.User;

import java.util.Optional;

public interface UserDao {
    int create(String username, String password);
    boolean existByUsername(String username);
    Optional<User> findByUsername(String username);
    void delete(Long id);
}
