package com.javarush.lapkinu.textquest.controller;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@WebServlet("/api/current-time")
public class CurrentTimeServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        try {
            Thread.sleep(0);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        LocalDateTime currentTime = LocalDateTime.now();
        String formattedTime = currentTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String jsonResponse = String.format("{\"time\": \"%s\"}", formattedTime);
        resp.getWriter().write(jsonResponse);
    }
}