package com.example.coursework.domain;

public class File {
    private Long id;
    private String path;
    private String text;
    private Long userId;

    public File(Long id, String path, String text, Long userId) {
        this.id = id;
        this.path = path;
        this.text = text;
        this.userId = userId;
    }

    public File() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
