package com.javarush.lapkinu.textquest.model.user;

import java.io.Serializable;

public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    private String username;
    private String password; // В реальном приложении храните хеш пароля

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Геттеры и сеттеры
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    // В реальном приложении не добавляйте сеттер для пароля или храните хеш пароля
    public void setPassword(String password) {
        this.password = password;
    }
}