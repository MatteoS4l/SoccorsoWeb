package com.soccorsoweb.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import com.soccorsoweb.model.Utente;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.soccorsoweb.model.enums.Ruolo;

@WebServlet(name = "HomeServlet", urlPatterns = { "/home", "" })
public class HomeServlet extends BaseServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("currentUser") != null) {
            Utente user = (Utente) session.getAttribute("currentUser");
            if (user.getRuolo() == Ruolo.ADMIN) {
                response.sendRedirect(request.getContextPath() + "/dashboard");
                return;
            } else {
                response.sendRedirect(request.getContextPath() + "/missioni");
                return;
            }
        }
        Map<String, Object> data = new HashMap<>();
        data.put("title", "Benvenuto su SoccorsoWeb");
        data.put("message", "La tua piattaforma per la gestione delle richieste di soccorso.");
        data.put("contextPath", request.getContextPath());
        processTemplate("index.ftl", data, request, response);
    }
}
