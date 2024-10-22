package com.javarush.lapkinu.textquest.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.javarush.lapkinu.textquest.model.user.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.BufferedReader;
import java.io.IOException;

@WebServlet("/api/login")
public class LoginServlet extends HttpServlet {

    private UserService userService;

    @Override
    public void init() {
        String usersFilePath = getServletContext().getRealPath("/data/users.properties");
        userService = new UserService(usersFilePath);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        BufferedReader reader = req.getReader();
        StringBuilder jsonInput = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            jsonInput.append(line);
        }
        Gson gson = new Gson();
        JsonObject jsonObject;
        try {
            jsonObject = gson.fromJson(jsonInput.toString(), JsonObject.class);
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\": \"Некорректный формат JSON\"}");
            return;
        }
        String username = jsonObject.get("username").getAsString();
        String password = jsonObject.get("password").getAsString();
        if (userService.authenticate(username, password)) {
            HttpSession session = req.getSession();
            session.setAttribute("username", username);
            resp.getWriter().write("{\"status\": \"success\"}");
        } else {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.getWriter().write("{\"error\": \"Неверное имя пользователя или пароль\"}");
        }
    }
}
