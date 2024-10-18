package com.javarush.lapkinu.textquest.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.javarush.lapkinu.textquest.model.quest.Node;
import com.javarush.lapkinu.textquest.model.quest.Player;
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
import java.util.stream.Collectors;

@WebServlet("/api/test-quest")
public class TestQuestServlet extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(TestQuestServlet.class);
    private QuestService questService;
    private SessionService sessionService;
    private PlayerActionService playerActionService;
    private EffectService effectService;

    @Override
    public void init() throws ServletException {
        questService = new QuestService("quests_4.json");
        sessionService = new SessionService();
        playerActionService = new PlayerActionService();
        effectService = new EffectService(questService);
        logger.info("TestQuestServlet initialized.");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Player player = sessionService.getPlayerFromSession(req, questService);
        if (player == null) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.getWriter().write("{\"error\": \"Unauthorized or starting location not found.\"}");
            return;
        }

        // Get locationId from request
        String locationId = req.getParameter("locationId");

        if (locationId != null && !locationId.isEmpty()) {
            // If locationId is provided, move the player
            String result = playerActionService.movePlayer(locationId, player, questService);
            Map<String, Object> response = new HashMap<>();
            response.put("message", result);
            resp.setContentType("application/json; charset=UTF-8");
            resp.getWriter().write(new Gson().toJson(response));
        } else {
            // If locationId is not provided, return the current game state
            Node currentNode = questService.getCurrentNode(player);
            if (currentNode == null) {
                logger.warn("Current location not found for player.");
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("{\"error\": \"Current location not found.\"}");
                return;
            }

            // Build the game state response
            Map<String, Object> gameState = new HashMap<>();
            gameState.put("location", currentNode);
            gameState.put("inventory", player.getInventory());
            gameState.put("neighbors", currentNode.getNeighbors().stream()
                    .map(questService::getLocation)
                    .collect(Collectors.toList()));
            gameState.put("locationItems", currentNode.getItems());
            gameState.put("actions", currentNode.getActions());
            gameState.put("health", player.getHealth());

            resp.setContentType("application/json; charset=UTF-8");
            resp.getWriter().write(new Gson().toJson(gameState));
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        // Step 1: Get player from session
        Player player = sessionService.getPlayerFromSession(req, questService);
        if (player == null) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.getWriter().write("{\"error\": \"Unauthorized\"}");
            return;
        }

        // Step 2: Read JSON from request body
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = req.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        }

        // Logging for request body
        logger.info("Request body: {}", sb.toString());

        // Step 3: Parse JSON
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(sb.toString(), JsonObject.class);

        // Logging for JSON content
        logger.info("Parsed JSON object: {}", jsonObject.toString());

        // Step 4: Check for 'action' parameter
        if (!jsonObject.has("action") || jsonObject.get("action").isJsonNull()) {
            logger.warn("Parameter 'action' is missing or null.");
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\": \"Action not specified.\"}");
            return;
        }

        // Step 5: Get 'action' and 'itemId' from JSON
        String actionDescription = jsonObject.get("action").getAsString();
        String itemId = jsonObject.has("itemId") && !jsonObject.get("itemId").isJsonNull()
                ? jsonObject.get("itemId").getAsString()
                : null;

        logger.info("Received action: '{}', itemId: '{}'", actionDescription, itemId);

        // Step 6: Handle player action
        String result;
        if (itemId != null) {
            // Handle item actions or movement
            result = playerActionService.handleItemAction(actionDescription, itemId, player, questService);
        } else {
            // Handle other actions
            result = playerActionService.handlePlayerAction(actionDescription, player, questService, effectService);
        }

        // Return result to client
        Map<String, Object> response = new HashMap<>();
        response.put("message", result);
        resp.setContentType("application/json; charset=UTF-8");
        resp.getWriter().write(new Gson().toJson(response));
    }
}
