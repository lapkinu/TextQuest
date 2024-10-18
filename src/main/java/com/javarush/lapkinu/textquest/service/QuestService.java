package com.javarush.lapkinu.textquest.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.javarush.lapkinu.textquest.model.quest.*;
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
                // Добавляем все локации в граф
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

    public Node getLocation(String locationId) {
        return questGraph.getNode(locationId);
    }

    public Graph getQuestGraph() {
        return questGraph;
    }

    // Метод для выполнения действия
    public String performAction(Player player, String actionDescription) {
        Node currentNode = questGraph.getNode(player.getCurrentNodeId());
        if (currentNode == null) {
            logger.error("Текущая локация '{}' игрока не найдена.", player.getCurrentNodeId());
            return "Текущая локация не найдена.";
        }

        // Поиск действия по описанию
        Action actionToPerform = currentNode.getActions().stream()
                .filter(a -> a.getDescription().equalsIgnoreCase(actionDescription))
                .findFirst()
                .orElse(null);

        if (actionToPerform != null) {
            String itemKey = actionToPerform.getItemKey();

            if (!player.hasItem(itemKey)) {
                logger.warn("Игроку не хватает предмета '{}' для выполнения действия '{}'.", itemKey, actionDescription);
                return "Для выполнения этого действия требуется предмет: " + itemKey;
            }

            // Применяем эффект действия и получаем сообщение
            String effectMessage = applyEffect(actionToPerform.getEffect(), player, currentNode);

            // Удаляем предмет из инвентаря, если это необходимо
            // Если хотите, можете добавить условие для удаления предмета после действия

            return effectMessage != null ? effectMessage : "Действие успешно выполнено.";
        }

        logger.warn("Действие '{}' не найдено в текущей локации '{}'.", actionDescription, currentNode.getId());
        return "Действие не найдено.";
    }

    // Метод для подбора предмета
    public String pickUpItem(Player player, String itemId) {
        Node currentNode = questGraph.getNode(player.getCurrentNodeId());
        if (currentNode == null) {
            logger.error("Текущая локация '{}' игрока не найдена.", player.getCurrentNodeId());
            return "Текущая локация не найдена.";
        }

        Item item = currentNode.getItems().stream()
                .filter(i -> i.getId().equalsIgnoreCase(itemId))
                .findFirst()
                .orElse(null);

        if (item == null) {
            logger.warn("Предмет '{}' не найден в текущей локации '{}'.", itemId, currentNode.getId());
            return "Предмет не найден в текущей локации.";
        }

        // Добавляем предмет в инвентарь и удаляем из локации
        player.addItem(item);
        currentNode.removeItem(itemId);
        logger.info("Игрок подобрал предмет '{}'.", itemId);

        return "Вы подобрали: " + item.getId();
    }

    // Метод для перемещения между локациями
    public String moveToLocation(Player player, String locationId) {
        Node currentNode = questGraph.getNode(player.getCurrentNodeId());
        if (currentNode == null) {
            logger.error("Текущая локация '{}' игрока не найдена.", player.getCurrentNodeId());
            return "Текущая локация не найдена.";
        }

        if (currentNode.getNeighbors().contains(locationId)) {
            Node nextNode = questGraph.getNode(locationId);
            if (nextNode != null) {
                player.setCurrentNodeId(nextNode.getId());
                logger.info("Игрок переместился в локацию '{}'.", nextNode.getId());
                return "Вы переместились в локацию: " + nextNode.getId();
            } else {
                logger.warn("Локация '{}' не найдена.", locationId);
                return "Локация '" + locationId + "' не найдена.";
            }
        }

        logger.warn("Невозможно переместиться в указанную локацию '{}'.", locationId);
        return "Невозможно переместиться в указанную локацию.";
    }

    // Метод для применения эффекта
    private String applyEffect(Effect effect, Player player, Node currentNode) {
        String type = effect.getType();
        String value = effect.getValue();

        switch (type) {
            case "increase_health":
                try {
                    int healthIncrease = Integer.parseInt(value);
                    player.increaseHealth(healthIncrease);
                    logger.info("Здоровье игрока увеличено на {} до {}.", healthIncrease, player.getHealth());
                    return "Ваше здоровье увеличено на " + healthIncrease + ".";
                } catch (NumberFormatException e) {
                    logger.error("Некорректное значение '{}' для увеличения здоровья.", value);
                    return "Ошибка при увеличении здоровья.";
                }
            case "add_neighbor":
                Node newNeighbor = questGraph.getNode(value);
                if (newNeighbor != null) {
                    currentNode.addNeighbor(newNeighbor.getId());
                    logger.info("Добавлен новый сосед '{}' для локации '{}'.", newNeighbor.getId(), currentNode.getId());
                    return "Открыт доступ к новой локации: " + newNeighbor.getId() + ".";
                } else {
                    logger.warn("Не удалось найти локацию '{}' для добавления в соседние.", value);
                    return "Не удалось открыть новую локацию.";
                }
            case "message":
                logger.info("Отображение сообщения игроку: '{}'.", value);
                return value;
            default:
                logger.warn("Неизвестный тип эффекта: '{}'.", type);
                return "Неизвестный эффект действия.";
        }
    }
}
