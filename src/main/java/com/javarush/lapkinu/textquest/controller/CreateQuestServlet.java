package com.javarush.lapkinu.textquest.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.javarush.lapkinu.textquest.model.quest.Node;
import com.javarush.lapkinu.textquest.model.quest.Quest;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.lang.reflect.Type;
import java.util.List;

@WebServlet("/api/create-quest")
public class CreateQuestServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(CreateQuestServlet.class);
    private Gson gson;
    private String questsFilePath = "quests_2.json"; // Путь к вашему JSON-файлу с квестами

    @Override
    public void init() throws ServletException {
        super.init();
        gson = new GsonBuilder().setPrettyPrinting().create();
        logger.info("CreateQuestServlet инициализирован.");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Установка типа контента для JSON
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        try {
            // Чтение данных из запроса
            BufferedReader reader = req.getReader();
            StringBuilder jsonInput = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonInput.append(line);
            }

            // Парсинг JSON
            Type questListType = new TypeToken<JsonObject>() {}.getType();
            JsonObject jsonObject = gson.fromJson(jsonInput.toString(), questListType);

            if (!jsonObject.has("locations")) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("{\"status\": \"failure\", \"error\": \"Отсутствует поле 'locations'.\"}");
                return;
            }

            // Извлечение списка локаций
            Type locationsType = new TypeToken<List<Node>>() {}.getType();
            List<Node> newLocations = gson.fromJson(jsonObject.get("locations"), locationsType);

            // Загрузка существующих квестов
            Quest existingQuest = loadExistingQuest();

            // Добавление новых локаций
            for (Node loc : newLocations) {
                // Проверка на уникальность ID
                if (existingQuest.getLocations().stream().anyMatch(existingLoc -> existingLoc.getId().equalsIgnoreCase(loc.getId()))) {
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    resp.getWriter().write("{\"status\": \"failure\", \"error\": \"Локация с ID '" + loc.getId() + "' уже существует.\"}");
                    return;
                }
                existingQuest.getLocations().add(loc);
            }

            // Сохранение обновлённого квеста
            saveQuest(existingQuest);

            // Отправка успешного ответа
            resp.getWriter().write("{\"status\": \"success\", \"message\": \"Квесты успешно обновлены.\"}");
            logger.info("Новые локации добавлены и сохранены.");

        } catch (Exception e) {
            logger.error("Ошибка при обработке POST-запроса /api/create-quest: {}", e.getMessage(), e);
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"status\": \"failure\", \"error\": \"Произошла внутренняя ошибка сервера.\"}");
        }
    }

    private Quest loadExistingQuest() throws IOException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(questsFilePath);
        if (inputStream == null) {
            throw new FileNotFoundException("Файл квестов не найден: " + questsFilePath);
        }
        Reader reader = new InputStreamReader(inputStream, "UTF-8");
        Type questType = new TypeToken<Quest>() {}.getType();
        Quest quest = gson.fromJson(reader, questType);
        reader.close();
        return quest;
    }

    private void saveQuest(Quest quest) throws IOException {
        // Получение пути к файлу
        String realPath = getServletContext().getRealPath("/" + questsFilePath);
        File file = new File(realPath);
        try (Writer writer = new OutputStreamWriter(new FileOutputStream(file), "UTF-8")) {
            gson.toJson(quest, writer);
        }
    }
}