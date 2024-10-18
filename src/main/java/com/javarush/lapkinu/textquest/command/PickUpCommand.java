package com.javarush.lapkinu.textquest.command;

import com.google.gson.JsonObject;
import com.javarush.lapkinu.textquest.model.quest.Player;
import com.javarush.lapkinu.textquest.service.PlayerActionService;
import com.javarush.lapkinu.textquest.service.QuestService;

public class PickUpCommand implements ActionCommand {
    private PlayerActionService playerActionService;
    private QuestService questService;

    public PickUpCommand(PlayerActionService playerActionService, QuestService questService) {
        this.playerActionService = playerActionService;
        this.questService = questService;
    }

    @Override
    public String execute(Player player, JsonObject jsonObject) {
        String itemId = jsonObject.get("itemId").getAsString();
        return playerActionService.pickUpItem(itemId, player, questService);
    }
}
