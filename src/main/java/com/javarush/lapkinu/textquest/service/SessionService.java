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
            // Create a new player
            player = new Player();

            // Set player's current location to the starting node
            Node startNode = questService.getStartNode();
            if (startNode == null) {
                logger.error("Starting location not found.");
                return null;
            }
            player.setCurrentNodeId(startNode.getId());

            session.setAttribute("player", player);
            logger.info("New player created and set in session.");
        }
        return player;
    }
}
