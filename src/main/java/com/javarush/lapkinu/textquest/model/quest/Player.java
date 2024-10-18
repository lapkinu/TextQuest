package com.javarush.lapkinu.textquest.model.quest;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Player implements Serializable {
    private static final long serialVersionUID = 1L;

    private String currentNodeId;       // Current location identifier
    private List<String> inventory;     // Player's inventory (list of item IDs)
    private int health;                 // Player's health

    public Player() {
        this.inventory = new ArrayList<>();
        this.health = 100;
    }

    // Getters and Setters
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

    // Methods to modify health
    public void increaseHealth(int amount) {
        this.health = Math.min(this.health + amount, 100);
    }

    public void decreaseHealth(int amount) {
        this.health = Math.max(this.health - amount, 0);
    }

    // Methods to manage inventory
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
