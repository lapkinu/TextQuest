package com.javarush.lapkinu.textquest.service;

import com.javarush.lapkinu.textquest.model.quest.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class PlayerActionService {

    private static final Logger logger = LoggerFactory.getLogger(PlayerActionService.class);

    public String movePlayer(String locationId, Player player, QuestService questService) {
        Node currentNode = questService.getCurrentNode(player);
        if (currentNode == null) {
            logger.warn("Текущая локация не найдена для игрока.");
            return "Ошибка: текущая локация не найдена.";
        }

        Node nextNode = questService.getLocation(locationId);
        if (nextNode == null || !currentNode.getNeighbors().contains(locationId)) {
            logger.warn("Невозможно переместиться в локацию '{}'.", locationId);
            return "Невозможно переместиться в указанную локацию.";
        }

        player.setCurrentNodeId(nextNode.getId());
        logger.info("Игрок перемещён в локацию '{}'.", locationId);
        return "Вы переместились в локацию: " + nextNode.getId() + ".";
    }

    public String handlePlayerAction(String actionDescription, Player player, QuestService questService, EffectService effectService) {
        Node currentNode = questService.getCurrentNode(player);
        if (currentNode == null) {
            logger.warn("Текущая локация не найдена для игрока.");
            return "Ошибка: текущая локация не найдена.";
        }

        // Ищем действие в текущей локации по описанию
        Optional<Action> actionOptional = currentNode.getActions().stream()
                .filter(action -> action.getDescription().equals(actionDescription))
                .findFirst();

        if (!actionOptional.isPresent()) {
            logger.warn("Действие '{}' не найдено в текущей локации.", actionDescription);
            return "Действие не найдено.";
        }

        Action action = actionOptional.get();

        // Проверяем, есть ли необходимый предмет у игрока
        if (action.getItemKey() != null && !player.hasItem(action.getItemKey())) {
            logger.warn("Игрок не имеет необходимого предмета '{}' для действия '{}'.", action.getItemKey(), actionDescription);
            return "У вас нет необходимого предмета для этого действия.";
        }

        // Применяем эффект действия
        return effectService.applyEffect(action.getEffect(), player);
    }

    public String pickUpItem(String itemId, Player player, QuestService questService) {
        Node currentNode = questService.getCurrentNode(player);
        if (currentNode == null) {
            logger.warn("Текущая локация не найдена для игрока.");
            return "Ошибка: текущая локация не найдена.";
        }

        // Ищем предмет в текущей локации
        Optional<Item> itemOptional = currentNode.getItems().stream()
                .filter(item -> item.getId().equals(itemId))
                .findFirst();

        if (!itemOptional.isPresent()) {
            logger.warn("Предмет '{}' не найден в текущей локации.", itemId);
            return "Предмет не найден в текущей локации.";
        }

        Item item = itemOptional.get();

        // Добавляем предмет в инвентарь игрока
        player.addItem(item.getId());

        // Удаляем предмет из локации
        currentNode.getItems().remove(item);

        logger.info("Игрок подобрал предмет '{}'.", itemId);
        return "Вы подобрали предмет: " + itemId;
    }
}
