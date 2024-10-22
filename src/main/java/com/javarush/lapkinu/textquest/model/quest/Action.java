package com.javarush.lapkinu.textquest.model.quest;

import java.io.Serializable;

public class Action implements Serializable {
    private static final long serialVersionUID = 1L;

    private String itemKey;
    private String description;
    private Effect effect;

    public Action() {}

    public Action(String itemKey, String description, Effect effect) {
        this.itemKey = itemKey;
        this.description = description;
        this.effect = effect;
    }


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
