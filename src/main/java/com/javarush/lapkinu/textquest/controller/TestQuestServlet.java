package com.javarush.lapkinu.textquest.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.javarush.lapkinu.textquest.command.ActionCommand;
import com.javarush.lapkinu.textquest.command.ActionCommandFactory;
import com.javarush.lapkinu.textquest.model.quest.GameState;
import com.javarush.lapkinu.textquest.model.quest.Player;
import com.javarush.lapkinu.textquest.model.response.GameResponse;
import com.javarush.lapkinu.textquest.service.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.BufferedReader;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/api/test-quest")
public class TestQuestServlet extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(TestQuestServlet.class);
    private QuestService questService;
    private SessionService sessionService;
    private PlayerActionService playerActionService;
    private EffectService effectService;
    private GameService gameService;
    private ActionCommandFactory commandFactory;

    @Override
    public void init() throws ServletException {
        questService = new QuestService("quests_4.json");
        sessionService = new SessionService();
        playerActionService = new PlayerActionService();
        effectService = new EffectService(questService);
        gameService = new GameService(questService);
        commandFactory = new ActionCommandFactory(playerActionService, questService, effectService);
        logger.info("TestQuestServlet инициализирован.");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Player player = sessionService.getPlayerFromSession(req, questService);
        if (player == null) {
            sendError(resp, HttpServletResponse.SC_UNAUTHORIZED, "Вы не авторизованы или начальная локация не найдена.");
            return;
        }

        String locationId = req.getParameter("locationId");
        Map<String, Object> responseData = new HashMap<>();

        if (locationId != null && !locationId.isEmpty()) {
            // Если указан locationId, перемещаем игрока
            String message = playerActionService.movePlayer(locationId, player, questService);
            responseData.put("message", message);
        } else {
            // Иначе возвращаем текущее состояние игры
            GameState gameState = gameService.getCurrentGameState(player);

            // Распаковываем данные из gameState без обёртки в объект gameState
            responseData.put("location", gameState.getLocation());
            responseData.put("inventory", gameState.getInventory());
            responseData.put("neighbors", gameState.getNeighbors());
            responseData.put("locationItems", gameState.getLocationItems());
            responseData.put("actions", gameState.getActions());
            responseData.put("health", gameState.getHealth());
        }

        sendResponse(resp, responseData);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Player player = sessionService.getPlayerFromSession(req, questService);
        if (player == null) {
            sendError(resp, HttpServletResponse.SC_UNAUTHORIZED, "Вы не авторизованы");
            return;
        }

        JsonObject jsonObject = parseRequestBody(req);
        if (jsonObject == null || !jsonObject.has("action") || jsonObject.get("action").isJsonNull()) {
            sendError(resp, HttpServletResponse.SC_BAD_REQUEST, "Действие не указано.");
            return;
        }

        String actionType = jsonObject.get("action").getAsString();

        ActionCommand command = commandFactory.getCommand(actionType);

        String result = command.execute(player, jsonObject);

        GameResponse gameResponse = new GameResponse(result);
        sendResponse(resp, gameResponse);
    }

    // Вспомогательные методы
    private void sendError(HttpServletResponse resp, int statusCode, String message) throws IOException {
        resp.setStatus(statusCode);
        resp.setContentType("application/json; charset=UTF-8");
        resp.getWriter().write("{\"error\": \"" + message + "\"}");
    }

    private void sendResponse(HttpServletResponse resp, Object data) throws IOException {
        resp.setContentType("application/json; charset=UTF-8");
        String jsonResponse = new Gson().toJson(data);
        resp.getWriter().write(jsonResponse);
    }

    private JsonObject parseRequestBody(HttpServletRequest req) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = req.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        }
        if (sb.length() == 0) {
            return null;
        }
        return JsonParser.parseString(sb.toString()).getAsJsonObject();
    }
}
