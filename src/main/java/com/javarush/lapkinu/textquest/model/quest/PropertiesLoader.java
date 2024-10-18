package com.javarush.lapkinu.textquest.model.quest;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class PropertiesLoader {

    public Properties loadProperties(String fileName) throws IOException {
        Properties properties = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(fileName)) {
            if (input == null) {
                throw new FileNotFoundException("Файл '" + fileName + "' не найден в classpath");
            }
            InputStreamReader reader = new InputStreamReader(input, StandardCharsets.UTF_8);
            properties.load(reader);
        }
        return properties;
    }
}