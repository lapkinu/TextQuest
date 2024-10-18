package com.javarush.lapkinu.textquest.model.quest;

import java.util.List;

public class GameState {
    private Node location;
    private List<String> inventory;
    private List<Node> neighbors;
    private List<Item> locationItems;
    private List<Action> actions;
    private int health;

    // Геттеры и сеттеры
    public Node getLocation() {
        return location;
    }

    public void setLocation(Node location) {
        this.location = location;
    }

    public List<String> getInventory() {
        return inventory;
    }

    public void setInventory(List<String> inventory) {
        this.inventory = inventory;
    }

    public List<Node> getNeighbors() {
        return neighbors;
    }

    public void setNeighbors(List<Node> neighbors) {
        this.neighbors = neighbors;
    }

    public List<Item> getLocationItems() {
        return locationItems;
    }

    public void setLocationItems(List<Item> locationItems) {
        this.locationItems = locationItems;
    }

    public List<Action> getActions() {
        return actions;
    }

    public void setActions(List<Action> actions) {
        this.actions = actions;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }
}
