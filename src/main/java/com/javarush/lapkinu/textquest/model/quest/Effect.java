package com.javarush.lapkinu.textquest.model.quest;

import java.io.Serializable;

public class Effect implements Serializable {
    private static final long serialVersionUID = 1L;

    private String type;  // Тип эффекта, например, "increase_health", "add_neighbor"
    private String value; // Значение эффекта, например, "20", "newLocation"

    public Effect() {}

    public Effect(String type, String value) {
        this.type = type;
        this.value = value;
    }

    // Геттеры и сеттеры
    public String getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
