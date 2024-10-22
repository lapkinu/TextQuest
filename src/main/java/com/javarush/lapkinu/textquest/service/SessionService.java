package com.javarush.lapkinu.textquest.service;

import com.javarush.lapkinu.textquest.model.quest.Node;
import com.javarush.lapkinu.textquest.model.quest.Player;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SessionService {

    private static final Logger logger = LoggerFactory.getLogger(SessionService.class);

    public Player getPlayerFromSession(HttpServletRequest req, QuestService questService) {
        HttpSession session = req.getSession();
        Player player = (Player) session.getAttribute("player");
        if (player == null) {
            player = new Player();
            Node startNode = questService.getStartNode();
            if (startNode == null) {
                logger.error("Стартовая локация не найдена.");
                return null;
            }
            player.setCurrentNodeId(startNode.getId());
            session.setAttribute("player", player);
            logger.info("Создан новый игрок и установлен в сессии.");
        }
        return player;
    }
}
