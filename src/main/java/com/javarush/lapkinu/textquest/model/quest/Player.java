package com.javarush.lapkinu.textquest.model.quest;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Player implements Serializable {
    private static final long serialVersionUID = 1L;

    private String currentNodeId;       // Идентификатор текущей локации
    private List<Item> inventory;
    private int health;

    public Player() {}

    public Player(String startNodeId) {
        this.currentNodeId = startNodeId;
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

    public List<Item> getInventory() {
        return inventory;
    }

    public void setInventory(List<Item> inventory) {
        this.inventory = inventory;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int prusHealth) {
        if (this.health < 100) {
            this.health = health + prusHealth;
        }
    }
    public void setRemoveHealth(int removeHealth) {
        this.health = health - removeHealth;
    }

    public boolean hasItem(String itemId) {
        for (Item item : inventory) {
            if (item.getId().equalsIgnoreCase(itemId)) {
                return true;
            }
        }
        return false;
    }

    public void removeItem(String itemId) {
        inventory.removeIf(item -> item.getId().equalsIgnoreCase(itemId));
    }

    public void addItem(Item item) {
        inventory.add(item);
    }

    public void increaseHealth(int amount) {
        this.health += amount;
        System.out.println("Ваше здоровье увеличилось до " + this.health);
    }
}
