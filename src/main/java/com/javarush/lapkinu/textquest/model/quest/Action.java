package com.javarush.lapkinu.textquest.model.quest;

import java.io.Serializable;

public class Action implements Serializable {
    private static final long serialVersionUID = 1L;

    private String itemKey;      // Required item to perform the action
    private String description;  // Description of the action
    private Effect effect;       // Effect of the action

    public Action() {}

    public Action(String itemKey, String description, Effect effect) {
        this.itemKey = itemKey;
        this.description = description;
        this.effect = effect;
    }

    // Getters and Setters
    public String getItemKey() {
        return itemKey;
    }

    public void setItemKey(String itemKey) {
        this.itemKey = itemKey;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Effect getEffect() {
        return effect;
    }

    public void setEffect(Effect effect) {
        this.effect = effect;
    }
}
