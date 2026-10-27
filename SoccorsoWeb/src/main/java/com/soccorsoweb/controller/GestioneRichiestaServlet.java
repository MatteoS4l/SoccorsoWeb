package com.soccorsoweb.controller;

import com.soccorsoweb.data.DAOFactory;
import com.soccorsoweb.data.RichiestaSoccorsoDAO;
import com.soccorsoweb.model.RichiestaSoccorso;
import com.soccorsoweb.model.Utente;
import com.soccorsoweb.model.enums.Ruolo;
import com.soccorsoweb.model.enums.StatoRichiesta;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "GestioneRichiestaServlet", urlPatterns = { "/gestione-richiesta" })
public class GestioneRichiestaServlet extends BaseServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {


        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("currentUser") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        Utente utente = (Utente) session.getAttribute("currentUser");
        if (utente.getRuolo() != Ruolo.ADMIN) {
            response.sendRedirect(request.getContextPath() + "/dashboard");
            return;
        }

        String idStr = request.getParameter("id");
        if (idStr == null || idStr.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/dashboard");
            return;
        }

        try {
            Integer id = Integer.parseInt(idStr);
            RichiestaSoccorsoDAO richiestaDAO = DAOFactory.getRichiestaSoccorsoDAO(request);
            RichiestaSoccorso richiesta = richiestaDAO.findById(id);

            if (richiesta == null) {
                response.sendRedirect(request.getContextPath() + "/dashboard");
                return;
            }

            Map<String, Object> data = new HashMap<>();
            data.put("title", "Gestione Richiesta");
            data.put("richiesta", richiesta);
            data.put("contextPath", request.getContextPath());

            data.put("user", session.getAttribute("currentUser"));

            processTemplate("gestione_richiesta.ftl", data, request, response);

        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/dashboard");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("currentUser") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        Utente utente = (Utente) session.getAttribute("currentUser");
        if (utente.getRuolo() != Ruolo.ADMIN) {
            response.sendRedirect(request.getContextPath() + "/dashboard");
            return;
        }

        String idStr = request.getParameter("id");
        String azione = request.getParameter("azione");

        if (idStr == null || azione == null) {
            response.sendRedirect(request.getContextPath() + "/dashboard");
            return;
        }

        try {
            Integer id = Integer.parseInt(idStr);
            RichiestaSoccorsoDAO richiestaDAO = DAOFactory.getRichiestaSoccorsoDAO(request);
            RichiestaSoccorso richiesta = richiestaDAO.findById(id);

            if (richiesta == null) {
                response.sendRedirect(request.getContextPath() + "/dashboard");
                return;
            }

            if ("RIFIUTA".equals(azione)) {
                richiesta.setStato(StatoRichiesta.RIFIUTATA);
                richiestaDAO.save(richiesta); 


                response.sendRedirect(request.getContextPath() + "/dashboard");

            } else {
                response.sendRedirect(request.getContextPath() + "/dashboard");
            }

        } catch (Exception e) {
            response.sendRedirect(request.getContextPath() + "/dashboard");
        }
    }
}
