package com.javarush.lapkinu.textquest.model.quest;

import java.io.Serializable;
import java.util.List;

public class Quest implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<Node> locations;

    public Quest() {}


    public List<Node> getLocations() {
        return locations;
    }

    public void setLocations(List<Node> locations) {
        this.locations = locations;
    }
}
