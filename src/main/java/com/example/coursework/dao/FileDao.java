package com.example.coursework.dao;

import com.example.coursework.domain.File;

public interface FileDao {
    void create(String path, int userId);
    File findByFilePath(String path);
    void delete(Long id);
}
