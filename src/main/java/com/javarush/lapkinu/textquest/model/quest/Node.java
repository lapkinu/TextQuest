package com.javarush.lapkinu.textquest.model.quest;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Node implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;                         // Location identifier
    private String description;                // Location description
    private List<String> neighbors;            // Neighboring locations
    private List<Item> items;                  // Items in the location
    private List<Action> actions;              // Actions available in the location

    public Node() {
        this.neighbors = new ArrayList<>();
        this.items = new ArrayList<>();
        this.actions = new ArrayList<>();
    }

    public Node(String id, String description) {
        this.id = id;
        this.description = description;
        this.neighbors = new ArrayList<>();
        this.items = new ArrayList<>();
        this.actions = new ArrayList<>();
    }

    // Methods to add and remove neighbors, items, and actions
    public void addNeighbor(String neighborId) {
        neighbors.add(neighborId);
    }

    public List<String> getNeighbors() {
        return neighbors;
    }

    public void addItem(Item item) {
        items.add(item);
    }

    public List<Item> getItems() {
        return items;
    }

    public void removeItem(String itemId) {
        items.removeIf(item -> item.getId().equalsIgnoreCase(itemId));
    }

    public void addAction(Action action) {
        actions.add(action);
    }

    public List<Action> getActions() {
        return actions;
    }

    // Getters and Setters for id and description
    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
