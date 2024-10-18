package com.javarush.lapkinu.textquest.command;

import com.google.gson.JsonObject;
import com.javarush.lapkinu.textquest.model.quest.Player;
import com.javarush.lapkinu.textquest.service.EffectService;
import com.javarush.lapkinu.textquest.service.PlayerActionService;
import com.javarush.lapkinu.textquest.service.QuestService;

public class DefaultActionCommand implements ActionCommand {
    private PlayerActionService playerActionService;
    private QuestService questService;
    private EffectService effectService;

    public DefaultActionCommand(PlayerActionService playerActionService, QuestService questService, EffectService effectService) {
        this.playerActionService = playerActionService;
        this.questService = questService;
        this.effectService = effectService;
    }

    @Override
    public String execute(Player player, JsonObject jsonObject) {
        String actionDescription = jsonObject.get("action").getAsString();
        return playerActionService.handlePlayerAction(actionDescription, player, questService, effectService);
    }
}
