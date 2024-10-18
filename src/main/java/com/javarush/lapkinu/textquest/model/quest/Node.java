package com.javarush.lapkinu.textquest.model.quest;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Node implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;                         // Идентификатор локации
    private String description;                // Описание локации
    private List<String> neighbors;            // Соседи по ключу локации
    private List<Item> items;                  // Предметы в локации
    private List<Action> actions;              // Действия в локации

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

    // Методы для работы с соседями
    public void addNeighbor(String neighborId) {
        neighbors.add(neighborId);
    }

    public List<String> getNeighbors() {
        return neighbors;
    }

    // Методы для работы с предметами
    public void addItem(Item item) {
        items.add(item);
    }

    public List<Item> getItems() {
        return items;
    }

    public void removeItem(String itemId) {
        items.removeIf(item -> item.getId().equalsIgnoreCase(itemId));
    }

    // Методы для работы с действиями
    public void addAction(Action action) {
        actions.add(action);
    }

    public List<Action> getActions() {
        return actions;
    }

    // Геттеры и сеттеры
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
