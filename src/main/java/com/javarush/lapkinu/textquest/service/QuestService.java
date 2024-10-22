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

                logger.info("Квесты успешно загружены из '{}'.", jsonFilePath);
            } else {
                throw new IllegalStateException("Нет данных в JSON-файле.");
            }

        } catch (Exception e) {
            logger.error("Ошибка при загрузке квестов из '{}': {}", jsonFilePath, e.getMessage(), e);
        }
    }

    public Node getStartNode() {
        return questGraph.getNodes().values().stream().findFirst().orElse(null);
    }

    public Node getLocation(String locationId) {
        return questGraph.getNode(locationId);
    }

    public Node getCurrentNode(Player player) {
        return getLocation(player.getCurrentNodeId());
    }

    public void addLocation(Node node) {
        questGraph.addNode(node);
    }

}
