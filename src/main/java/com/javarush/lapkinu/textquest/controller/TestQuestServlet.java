package com.javarush.lapkinu.textquest.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.javarush.lapkinu.textquest.model.quest.Action;
import com.javarush.lapkinu.textquest.model.quest.Item;
import com.javarush.lapkinu.textquest.model.quest.Node;
import com.javarush.lapkinu.textquest.model.quest.Player;
import com.javarush.lapkinu.textquest.service.QuestService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@WebServlet("/api/test-quest")
public class TestQuestServlet extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(TestQuestServlet.class);

    private QuestService questService;

    @Override
    public void init() throws ServletException {
        super.init();
        // Инициализируем сервис с путем к JSON файлу
        questService = new QuestService("quests_4.json");
        logger.info("TestQuestServlet инициализирован.");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            HttpSession session = req.getSession(false);
            if (session == null || session.getAttribute("username") == null) {
                logger.warn("Неавторизованный доступ к /api/test-quest.");
                resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                resp.getWriter().write("{\"error\": \"Вы не авторизованы\"}");
                return;
            }

            Player player = (Player) session.getAttribute("player");
            if (player == null) {
                // Установка начальной локации для игрока
                String startNodeId = "Космический корабль";  // Используйте реальный ключ стартовой локации
                Node startNode = questService.getLocation(startNodeId);
                if (startNode == null) {
                    logger.error("Стартовая комната '{}' не найдена.", startNodeId);
                    throw new IllegalStateException("Стартовая комната не найдена.");
                }
                player = new Player(startNode.getId());
                session.setAttribute("player", player);
                logger.info("Игроку назначена начальная локация: {}", startNodeId);
            }

            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");

            // Получаем текущую локацию игрока
            Node currentNode = questService.getLocation(player.getCurrentNodeId());
            if (currentNode == null) {
                logger.error("Текущая локация '{}' игрока не найдена.", player.getCurrentNodeId());
                throw new IllegalStateException("Текущая локация игрока не найдена.");
            }

            // Формируем данные для ответа
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("location", Map.of(
                    "id", currentNode.getId(),
                    "description", currentNode.getDescription()
            ));
            responseData.put("inventory", getInventoryIds(player.getInventory()));  // Инвентарь игрока

            // Соседние локации
            List<Map<String, String>> neighbors = currentNode.getNeighbors().stream()
                    .map(neighborId -> Map.of(
                            "id", neighborId,
                            "name", neighborId
                    ))
                    .collect(Collectors.toList());
            responseData.put("neighbors", neighbors);

            // Добавляем здоровье игрока
            responseData.put("health", player.getHealth());

            // Добавляем предметы в текущей локации
            List<Item> locationItems = currentNode.getItems();
            List<Map<String, String>> itemsInLocation = locationItems.stream()
                    .map(item -> Map.of(
                            "id", item.getId(),
                            "description", item.getDescription()
                    ))
                    .collect(Collectors.toList());
            responseData.put("locationItems", itemsInLocation);

            // Добавляем действия текущей локации
            List<Action> actions = currentNode.getActions();
            List<Map<String, String>> availableActions = actions.stream()
                    .map(action -> Map.of(
                            "id", action.getItemKey(),
                            "description", action.getDescription()
                    ))
                    .collect(Collectors.toList());
            responseData.put("actions", availableActions);

            // Преобразуем данные в JSON и отправляем
            Gson gson = new Gson();
            String jsonResponse = gson.toJson(responseData);
            resp.getWriter().write(jsonResponse);
            logger.debug("Отправлены данные локации '{}' игроку.", currentNode.getId());

        } catch (Exception e) {
            logger.error("Произошла ошибка при обработке GET-запроса /api/test-quest: {}", e.getMessage(), e);
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"error\": \"Произошла ошибка на сервере: " + e.getMessage() + "\"}");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        // Установка типа контента для JSON
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        HttpSession session = req.getSession();
        Player player = (Player) session.getAttribute("player");
        if (player == null) {
            logger.warn("POST-запрос к /api/test-quest от неинициализированного игрока.");
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\": \"Игрок не инициализирован.\"}");
            return;
        }

        // Чтение данных из запроса
        BufferedReader reader = req.getReader();
        StringBuilder jsonInput = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            jsonInput.append(line);
        }

        // Парсинг JSON
        Gson gson = new Gson();
        JsonObject jsonObject;
        try {
            jsonObject = gson.fromJson(jsonInput.toString(), JsonObject.class);
        } catch (Exception e) {
            logger.error("Некорректный формат JSON в POST-запросе /api/test-quest: {}", e.getMessage(), e);
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\": \"Некорректный формат JSON\"}");
            return;
        }

        // Получаем действие из запроса
        String action = jsonObject.get("action").getAsString();
        logger.info("Получено действие: {}", action);

        // Обработка действий
        String responseMessage = "";

        if (action.equalsIgnoreCase("pick_up")) {
            if (!jsonObject.has("itemId")) {
                logger.warn("POST-запрос к /api/test-quest с действием 'pick_up' без 'itemId'.");
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("{\"error\": \"Не указан itemId для подбора предмета.\"}");
                return;
            }
            String itemId = jsonObject.get("itemId").getAsString();
            responseMessage = questService.pickUpItem(player, itemId);
            if (responseMessage.startsWith("Вы подобрали")) {
                session.setAttribute("player", player);
                // Возвращаем успешный ответ
                JsonObject responseJson = new JsonObject();
                responseJson.addProperty("status", "success");
                responseJson.addProperty("message", responseMessage);
                resp.getWriter().write(responseJson.toString());
                logger.info("Игрок подобрал предмет '{}'.", itemId);
            } else {
                logger.warn("Ошибка при подборе предмета '{}': {}", itemId, responseMessage);
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("{\"error\": \"" + responseMessage + "\"}");
            }
            return;
        }

        // Выполнение действия (не "pick_up")
        responseMessage = questService.performAction(player, action);
        if (!responseMessage.equals("Действие не найдено.") && !responseMessage.equals("Текущая локация не найдена.")) {
            session.setAttribute("player", player);
            // Возвращаем успешный ответ
            JsonObject responseJson = new JsonObject();
            responseJson.addProperty("status", "success");
            responseJson.addProperty("message", responseMessage);
            resp.getWriter().write(responseJson.toString());
            logger.info("Действие '{}' выполнено с сообщением: '{}'.", action, responseMessage);
        } else {
            // Если действие не найдено, пытаемся выполнить переход
            responseMessage = questService.moveToLocation(player, action);
            if (responseMessage.startsWith("Вы переместились")) {
                session.setAttribute("player", player);
                // Возвращаем успешный ответ
                JsonObject responseJson = new JsonObject();
                responseJson.addProperty("status", "success");
                responseJson.addProperty("message", responseMessage);
                resp.getWriter().write(responseJson.toString());
                logger.info("Игрок переместился в локацию '{}'.", action);
            } else {
                logger.warn("Ошибка при выполнении действия '{}': {}", action, responseMessage);
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("{\"error\": \"" + responseMessage + "\"}");
            }
        }
    }

    // Вспомогательный метод для получения идентификаторов предметов в инвентаре
    private List<String> getInventoryIds(List<Item> inventory) {
        return inventory.stream()
                .map(Item::getId)
                .collect(Collectors.toList());
    }
}
