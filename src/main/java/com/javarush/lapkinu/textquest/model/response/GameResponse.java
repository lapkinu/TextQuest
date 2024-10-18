package com.javarush.lapkinu.textquest.model.response;

import com.javarush.lapkinu.textquest.model.quest.GameState;

public class GameResponse {
    private String message;
    private GameState gameState;

    public GameResponse(String message) {
        this.message = message;
    }

    public GameResponse(GameState gameState) {
        this.gameState = gameState;
    }

    // Геттеры и сеттеры
    public String getMessage() {
        return message;
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }
}
