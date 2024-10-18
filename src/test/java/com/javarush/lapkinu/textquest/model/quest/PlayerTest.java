package com.javarush.lapkinu.textquest.model.quest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PlayerTest {

    private Player player;

    @BeforeEach
    public void setUp() {
        player = new Player("startRoom");
    }

    @Test
    public void testAddItem() {
        Item item = new Item("ключ", "Золотой ключ");
        player.addItem(item);
        assertThat(player.getInventory()).containsExactly(item);
    }

    @Test
    public void testRemoveItemExists() {
        Item item = new Item("ключ", "Золотой ключ");
        player.addItem(item);
        player.removeItem("ключ");
        assertThat(player.getInventory()).isEmpty();
    }

    @Test
    public void testRemoveItemDoesNotExist() {
        player.removeItem("неизвестныйПредмет");
        assertThat(player.getInventory()).isEmpty();
    }

    @Test
    public void testHasItem() {
        Item item = new Item("ключ", "Золотой ключ");
        player.addItem(item);
        assertThat(player.hasItem("ключ")).isTrue();
        assertThat(player.hasItem("зелье")).isFalse();
    }

    @Test
    public void testIncreaseHealth() {
        player.increaseHealth(20);
        assertThat(player.getHealth()).isEqualTo(120);
    }
}
