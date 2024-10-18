package com.javarush.lapkinu.textquest.command;

import com.google.gson.JsonObject;
import com.javarush.lapkinu.textquest.model.quest.Player;

public interface ActionCommand {
    String execute(Player player, JsonObject jsonObject);
}
