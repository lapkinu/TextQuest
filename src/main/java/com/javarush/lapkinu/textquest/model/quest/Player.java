package com.javarush.lapkinu.textquest.model.quest;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Player implements Serializable {
    private static final long serialVersionUID = 1L;

    private String currentNodeId;       // Идентификатор текущей локации
    private List<String> inventory;
    private int health;

    public Player() {
        this.inventory = new ArrayList<>();
        this.health = 100;
    }

    // Геттеры и сеттеры
    public String getCurrentNodeId() {
        return currentNodeId;
    }

    public void setCurrentNodeId(String nodeId) {
        this.currentNodeId = nodeId;
    }

    public List<String> getInventory() {
        return inventory;
    }

    public void setInventory(List<String> inventory) {
        this.inventory = inventory;
    }

    public int getHealth() {
        return health;
    }

    // Методы для управления здоровьем
    public void increaseHealth(int amount) {
        this.health = Math.min(this.health + amount, 100);
    }

    public void decreaseHealth(int amount) {
        this.health = Math.max(this.health - amount, 0);
    }

    // Методы для управления инвентарем
    public boolean hasItem(String itemId) {
        return inventory.contains(itemId);
    }

    public void addItem(String itemId) {
        if (!inventory.contains(itemId)) {
            inventory.add(itemId);
        }
    }

    public void removeItem(String itemId) {
        inventory.remove(itemId);
    }
}
