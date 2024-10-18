package com.javarush.lapkinu.textquest.model.quest;

import java.io.Serializable;

public class Action implements Serializable {
    private static final long serialVersionUID = 1L;

    private String itemKey;          // Ключ предмета, связанного с действием
    private String description;      // Описание действия
    private Effect effect;           // Эффект действия

    public Action() {}

    public Action(String itemKey, String description, Effect effect) {
        this.itemKey = itemKey;
        this.description = description;
        this.effect = effect;
    }

    // Геттеры и сеттеры
    public String getItemKey() {
        return itemKey;
    }

    public String getDescription() {
        return description;
    }

    public Effect getEffect() {
        return effect;
    }

    public void setItemKey(String itemKey) {
        this.itemKey = itemKey;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setEffect(Effect effect) {
        this.effect = effect;
    }
}
