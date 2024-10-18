package com.javarush.lapkinu.textquest.service;

import com.javarush.lapkinu.textquest.model.quest.Item;
import com.javarush.lapkinu.textquest.model.quest.Node;
import com.javarush.lapkinu.textquest.model.quest.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class QuestServiceTest {

    private QuestService questService;
    private Player player;

    @BeforeEach
    public void setUp() {
        questService = new QuestService("questsTest.json");
        player = new Player("hall");
        player.addItem(new Item("ключ", "Золотой ключ"));
    }

    @Test
    public void testPerformActionWithoutRequiredItem() {
        player.removeItem("ключ");
        String actionDescription = "Открыть дверь";
        String result = questService.performAction(player, actionDescription);
        assertThat(result).isEqualTo("Для выполнения этого действия требуется предмет: ключ");
    }

    @Test
    public void testPerformNonExistingAction() {
        String actionDescription = "Неизвестное действие";
        String result = questService.performAction(player, actionDescription);
        assertThat(result).isEqualTo("Действие не найдено.");
    }
}
