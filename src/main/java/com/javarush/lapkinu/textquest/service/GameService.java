package com.javarush.lapkinu.textquest.service;

import com.javarush.lapkinu.textquest.model.quest.*;

import java.util.stream.Collectors;

public class GameService {
    private QuestService questService;

    public GameService(QuestService questService) {
        this.questService = questService;
    }

    public GameState getCurrentGameState(Player player) {
        Node currentNode = questService.getCurrentNode(player);

        GameState gameState = new GameState();
        gameState.setLocation(currentNode);
        gameState.setInventory(player.getInventory());
        gameState.setNeighbors(currentNode.getNeighbors().stream()
                .map(questService::getLocation)
                .collect(Collectors.toList()));
        gameState.setLocationItems(currentNode.getItems());
        gameState.setActions(currentNode.getActions());
        gameState.setHealth(player.getHealth());
        return gameState;
    }
}
