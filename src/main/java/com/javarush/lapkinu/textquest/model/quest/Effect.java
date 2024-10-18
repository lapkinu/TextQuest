package com.javarush.lapkinu.textquest.model.quest;

import java.io.Serializable;

public class Effect implements Serializable {
    private static final long serialVersionUID = 1L;

    private String type;  // Type of effect
    private String value; // Value associated with the effect

    public Effect() {}

    public Effect(String type, String value) {
        this.type = type;
        this.value = value;
    }

    // Getters and Setters
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
