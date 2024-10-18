package com.javarush.lapkinu.textquest.service;

import com.javarush.lapkinu.textquest.model.quest.Action;
import com.javarush.lapkinu.textquest.model.quest.Item;
import com.javarush.lapkinu.textquest.model.quest.Node;
import com.javarush.lapkinu.textquest.model.quest.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class PlayerActionService {

    private static final Logger logger = LoggerFactory.getLogger(PlayerActionService.class);

    public String movePlayer(String locationId, Player player, QuestService questService) {
        Node currentNode = questService.getCurrentNode(player);
        if (currentNode == null) {
            logger.warn("Current location not found for player.");
            return "Error: Current location not found.";
        }

        Node nextNode = questService.getLocation(locationId);
        if (nextNode == null || !currentNode.getNeighbors().contains(locationId)) {
            logger.warn("Cannot move to location '{}'.", locationId);
            return "Cannot move to the specified location.";
        }

        player.setCurrentNodeId(nextNode.getId());
        logger.info("Player moved to location '{}'.", locationId);
        return "You moved to location: " + nextNode.getId() + ".";
    }

    public String handlePlayerAction(String actionDescription, Player player, QuestService questService, EffectService effectService) {
        Node currentNode = questService.getCurrentNode(player);
        if (currentNode == null) {
            logger.warn("Current location not found for player.");
            return "Error: Current location not found.";
        }

        // Find the action in the current location by description
        Optional<Action> actionOptional = currentNode.getActions().stream()
                .filter(action -> action.getDescription().equals(actionDescription))
                .findFirst();

        if (!actionOptional.isPresent()) {
            logger.warn("Action '{}' not found in current location.", actionDescription);
            return "Действие не найдено.";
        }

        Action action = actionOptional.get();

        // Check if the required item is in the player's inventory
        if (action.getItemKey() != null && !player.hasItem(action.getItemKey())) {
            logger.warn("Player does not have required item '{}' for action '{}'.", action.getItemKey(), actionDescription);
            return "У вас нет необходимого элемента в инвентаре для этого действия.";
        }

        // Apply the effect of the action
        return effectService.applyEffect(action.getEffect(), player);
    }

    public String handleItemAction(String actionType, String itemId, Player player, QuestService questService) {
        switch (actionType) {
            case "move":
                return movePlayer(itemId, player, questService);
            case "pick_up":
                return pickUpItem(itemId, player, questService);
            default:
                logger.warn("Unknown action type '{}'.", actionType);
                return "Unknown action.";
        }
    }

    private String pickUpItem(String itemId, Player player, QuestService questService) {
        Node currentNode = questService.getCurrentNode(player);
        if (currentNode == null) {
            logger.warn("Current location not found for player.");
            return "Error: Current location not found.";
        }

        // Find the item in the current location
        Optional<Item> itemOptional = currentNode.getItems().stream()
                .filter(item -> item.getId().equals(itemId))
                .findFirst();

        if (!itemOptional.isPresent()) {
            logger.warn("Item '{}' not found in current location.", itemId);
            return "Item not found in current location.";
        }

        Item item = itemOptional.get();

        // Add item to player's inventory
        player.addItem(item.getId());

        // Remove item from location
        currentNode.getItems().remove(item);

        logger.info("Player picked up item '{}'.", itemId);
        return "Предмет " + itemId + " перемещен в инвентарь";
    }
}
