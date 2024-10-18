package com.javarush.lapkinu.textquest.command;

import com.javarush.lapkinu.textquest.service.EffectService;
import com.javarush.lapkinu.textquest.service.PlayerActionService;
import com.javarush.lapkinu.textquest.service.QuestService;

import java.util.HashMap;
import java.util.Map;

public class ActionCommandFactory {
    private Map<String, ActionCommand> commands = new HashMap<>();

    public ActionCommandFactory(PlayerActionService playerActionService, QuestService questService, EffectService effectService) {
        commands.put("move", new MoveCommand(playerActionService, questService));
        commands.put("pick_up", new PickUpCommand(playerActionService, questService));
        // Добавьте другие команды по необходимости
        commands.put("default", new DefaultActionCommand(playerActionService, questService, effectService));
    }

    public ActionCommand getCommand(String actionType) {
        return commands.getOrDefault(actionType, commands.get("default"));
    }
}
