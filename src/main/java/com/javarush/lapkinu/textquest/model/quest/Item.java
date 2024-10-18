package com.javarush.lapkinu.textquest.model.quest;

import java.io.Serializable;

public class Item implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;            // Item identifier
    private String description;   // Item description

    public Item() {}

    public Item(String id, String description) {
        this.id = id;
        this.description = description;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
