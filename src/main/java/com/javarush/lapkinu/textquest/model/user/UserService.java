package com.javarush.lapkinu.textquest.model.user;

import java.io.*;
import java.util.Properties;

public class UserService {
    private Properties usersProperties;
    private String usersFilePath;

    public UserService(String usersFilePath) {
        this.usersFilePath = usersFilePath;
        loadUsers();
    }

    private void loadUsers() {
        usersProperties = new Properties();
        try (InputStream input = new FileInputStream(usersFilePath)) {
            usersProperties.load(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveUsers() {
        try (OutputStream output = new FileOutputStream(usersFilePath)) {
            usersProperties.store(output, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean registerUser(String username, String password) {
        if (usersProperties.containsKey(username)) {
            return false;
        }
        usersProperties.setProperty(username, password);
        saveUsers();
        return true;
    }

    public boolean authenticate(String username, String password) {
        String storedPassword = usersProperties.getProperty(username);
        return storedPassword != null && storedPassword.equals(password);
    }
}