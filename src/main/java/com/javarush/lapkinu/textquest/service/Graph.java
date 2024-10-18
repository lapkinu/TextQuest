package com.javarush.lapkinu.textquest.service;

import com.javarush.lapkinu.textquest.model.quest.Node;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Graph implements Serializable {
    private static final long serialVersionUID = 1L;
    private Map<String, Node> nodes;

    public Graph() {
        nodes = new HashMap<>();
    }

    public void addNode(Node node) {
        nodes.put(node.getId(), node);
    }

    public Node getNode(String id) {
        return nodes.get(id);
    }

    public Map<String, Node> getNodes() {
        return nodes;
    }
}
