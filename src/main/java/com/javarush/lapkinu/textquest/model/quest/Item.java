package com.javarush.lapkinu.textquest.model.quest;

import java.io.Serializable;

public class Item implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;            // Идентификатор предмета
    private String description;   // Описание предмета

    public Item() {}

    public Item(String id, String description) {
        this.id = id;
        this.description = description;
    }

    // Геттеры и сеттеры
    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
