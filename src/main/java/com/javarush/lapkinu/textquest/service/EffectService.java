package com.javarush.lapkinu.textquest.service;

import com.javarush.lapkinu.textquest.model.quest.Effect;
import com.javarush.lapkinu.textquest.model.quest.Node;
import com.javarush.lapkinu.textquest.model.quest.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

public class EffectService {

    private static final Logger logger = LoggerFactory.getLogger(EffectService.class);
    private final QuestService questService;

    public EffectService(QuestService questService) {
        this.questService = questService;
        initializeEffectHandlers();
    }

    private final Map<String, BiFunction<Effect, Player, String>> effectHandlers = new HashMap<>();

    private void initializeEffectHandlers() {
        effectHandlers.put("increase_health", this::handleIncreaseHealth);
        effectHandlers.put("add_neighbor", this::handleAddNeighbor);
        effectHandlers.put("message", this::handleMessage);
    }

    public String applyEffect(Effect effect, Player player) {
        return effectHandlers.getOrDefault(effect.getType(), this::handleUnknownEffect)
                .apply(effect, player);
    }

    private String handleIncreaseHealth(Effect effect, Player player) {
        try {
            int healthIncrease = Integer.parseInt(effect.getValue());
            player.increaseHealth(healthIncrease);
            logger.info("Player health increased by {} to {}.", healthIncrease, player.getHealth());
            return "Ваше здоровье увеличилось на " + healthIncrease + ".";
        } catch (NumberFormatException e) {
            logger.error("Invalid health increase value '{}'.", effect.getValue());
            return "Error increasing health.";
        }
    }

    private String handleAddNeighbor(Effect effect, Player player) {
        Node currentNode = questService.getCurrentNode(player);
        if (currentNode == null) {
            logger.warn("Current location not found for player.");
            return "Error: Current location not found.";
        }

        Node newNeighbor = questService.getLocation(effect.getValue());
        if (newNeighbor != null) {
            currentNode.addNeighbor(newNeighbor.getId());
            logger.info("Added new neighbor '{}' to location '{}'.", newNeighbor.getId(), currentNode.getId());
            return "Открыта новая локация: " + newNeighbor.getId() + ".";
        } else {
            logger.warn("Location '{}' not found.", effect.getValue());
            return "Невозможно разблокировать новую локацию.";
        }
    }

    private String handleMessage(Effect effect, Player player) {
        logger.info("Message to player: {}", effect.getValue());
        return effect.getValue();
    }

    private String handleUnknownEffect(Effect effect, Player player) {
        logger.warn("Unknown effect type '{}'.", effect.getType());
        return "Unknown effect.";
    }
}
