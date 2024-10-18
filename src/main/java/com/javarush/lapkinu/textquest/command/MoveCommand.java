package com.javarush.lapkinu.textquest.command;

import com.google.gson.JsonObject;
import com.javarush.lapkinu.textquest.model.quest.Player;
import com.javarush.lapkinu.textquest.service.PlayerActionService;
import com.javarush.lapkinu.textquest.service.QuestService;

public class MoveCommand implements ActionCommand {
    private PlayerActionService playerActionService;
    private QuestService questService;

    public MoveCommand(PlayerActionService playerActionService, QuestService questService) {
        this.playerActionService = playerActionService;
        this.questService = questService;
    }

    @Override
    public String execute(Player player, JsonObject jsonObject) {
        String locationId = jsonObject.get("itemId").getAsString();
        return playerActionService.movePlayer(locationId, player, questService);
    }
}
