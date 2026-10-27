package com.soccorsoweb.controller;

import com.soccorsoweb.data.DAOFactory;
import com.soccorsoweb.data.MissioneDAO;
import com.soccorsoweb.model.Missione;
import com.soccorsoweb.model.Utente;
import com.soccorsoweb.model.enums.StatoMissione;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import com.soccorsoweb.model.enums.Ruolo;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "MissioniServlet", urlPatterns = { "/missioni" })
public class MissioniServlet extends BaseServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("currentUser") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        Utente currentUser = (Utente) session.getAttribute("currentUser");
        Map<String, Object> data = new HashMap<>();
        data.put("title", "Gestione Missioni");
        data.put("user", currentUser);
        data.put("contextPath", request.getContextPath());

        MissioneDAO missioneDAO = DAOFactory.getMissioneDAO(request);
        
        List<Missione> missioniAttive;
        List<Missione> missioniConcluse;

        if (currentUser.getRuolo() == Ruolo.ADMIN) {
            // L'admin vede tutto
            missioniAttive = missioneDAO.findByStato(StatoMissione.ATTIVA);
            missioniConcluse = missioneDAO.findByStato(StatoMissione.COMPLETATA);
            missioniConcluse.addAll(missioneDAO.findByStato(StatoMissione.ANNULLATA));
        } else {
            // L'operatore vede solo le sue missioni
            missioniAttive = missioneDAO.findByUser(currentUser.getId(), StatoMissione.ATTIVA);
            missioniConcluse = missioneDAO.findByUser(currentUser.getId(), StatoMissione.COMPLETATA);
        }

        data.put("missioniAttive", missioniAttive);
        data.put("missioniConcluse", missioniConcluse);

        processTemplate("missioni.ftl", data, request, response);
    }
}
