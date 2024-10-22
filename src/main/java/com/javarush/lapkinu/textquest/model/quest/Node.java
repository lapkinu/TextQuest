package com.javarush.lapkinu.textquest.model.quest;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Node implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String description;
    private List<String> neighbors;
    private List<Item> items;
    private List<Action> actions;

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
