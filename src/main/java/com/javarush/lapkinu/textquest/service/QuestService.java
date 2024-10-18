package com.javarush.lapkinu.textquest.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.javarush.lapkinu.textquest.model.quest.Node;
import com.javarush.lapkinu.textquest.model.quest.Player;
import com.javarush.lapkinu.textquest.model.quest.Quest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;

public class QuestService {

    private static final Logger logger = LoggerFactory.getLogger(QuestService.class);
    private Graph questGraph;

    public QuestService(String jsonFilePath) {
        questGraph = new Graph();
        loadQuests(jsonFilePath);
    }

    // Method to load quests from a JSON file
    private void loadQuests(String jsonFilePath) {
        try {
            Gson gson = new Gson();
            Reader reader = new InputStreamReader(
                    getClass().getClassLoader().getResourceAsStream(jsonFilePath), "UTF-8");

            Type questType = new TypeToken<Quest>() {}.getType();
            Quest questData = gson.fromJson(reader, questType);

            if (questData != null && questData.getLocations() != null) {
                for (Node node : questData.getLocations()) {
                    questGraph.addNode(node);
                }

                logger.info("Quests loaded successfully from '{}'.", jsonFilePath);
            } else {
                throw new IllegalStateException("No data in JSON file.");
            }

        } catch (Exception e) {
            logger.error("Error loading quests from '{}': {}", jsonFilePath, e.getMessage(), e);
        }
    }

    // Method to get the starting node
    public Node getStartNode() {
        return questGraph.getNodes().values().stream().findFirst().orElse(null);
    }

    // Method to get a location by its ID
    public Node getLocation(String locationId) {
        return questGraph.getNode(locationId);
    }

    // Method to get the player's current location
    public Node getCurrentNode(Player player) {
        return getLocation(player.getCurrentNodeId());
    }
}
