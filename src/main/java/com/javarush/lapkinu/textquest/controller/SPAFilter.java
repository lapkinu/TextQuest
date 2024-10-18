package com.javarush.lapkinu.textquest.controller;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebFilter("/*")
public class SPAFilter implements Filter {

    private static final String API_PREFIX = "/api/";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        String path = req.getRequestURI();
        if (path.startsWith(API_PREFIX) || path.startsWith("/static/") || path.contains(".") || path.startsWith("/assets/")) {
            chain.doFilter(request, response);
            return;
        }
        RequestDispatcher dispatcher = req.getRequestDispatcher("/index.html");
        dispatcher.forward(req, resp);
    }
}
