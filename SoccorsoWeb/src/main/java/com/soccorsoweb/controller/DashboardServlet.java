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
import java.util.List;
import java.util.Map;

@WebServlet(name = "DashboardServlet", urlPatterns = { "/dashboard" })
public class DashboardServlet extends BaseServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("currentUser") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        Utente currentUser = (Utente) session.getAttribute("currentUser");
        

        if (currentUser.getRuolo() != Ruolo.ADMIN) {
            renderErrorPage(request, response, "Accesso non autorizzato", "Non hai i permessi necessari per visualizzare la Dashboard Amministrativa.", 403);
            return;
        }

        Map<String, Object> data = new HashMap<>();
        data.put("title", "Dashboard Amministratore");
        data.put("user", currentUser);
        data.put("contextPath", request.getContextPath());

        RichiestaSoccorsoDAO richiestaDAO = DAOFactory.getRichiestaSoccorsoDAO(request);
        List<RichiestaSoccorso> richiesteInAttesa = richiestaDAO.findByStato(StatoRichiesta.IN_ATTESA);
        
        data.put("richieste", richiesteInAttesa);

        processTemplate("dashboard.ftl", data, request, response);
    }
}
