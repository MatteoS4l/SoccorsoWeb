package com.soccorsoweb.controller;

import com.soccorsoweb.data.DAOFactory;
import com.soccorsoweb.data.LogMissioneDAO;
import com.soccorsoweb.data.MissioneDAO;
import com.soccorsoweb.data.MezzoDAO;
import com.soccorsoweb.model.LogMissione;
import com.soccorsoweb.model.Missione;
import com.soccorsoweb.model.Mezzo;
import com.soccorsoweb.model.Utente;
import com.soccorsoweb.model.enums.Ruolo;
import com.soccorsoweb.model.enums.StatoMissione;
import com.soccorsoweb.model.enums.StatoMezzo;
import com.soccorsoweb.util.EmailService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "DettaglioMissioneServlet", urlPatterns = { "/dettaglio-missione" })
public class DettaglioMissioneServlet extends BaseServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("currentUser") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String idStr = request.getParameter("id");
        if (idStr == null || idStr.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/missioni");
            return;
        }

        try {
            Integer id = Integer.parseInt(idStr);
            MissioneDAO missioneDAO = DAOFactory.getMissioneDAO(request);
            LogMissioneDAO logDAO = DAOFactory.getLogMissioneDAO(request);
            
            Missione missione = missioneDAO.findById(id);
            if (missione == null) {
                renderErrorPage(request, response, "Missione non trovata", 404);
                return;
            }

            List<LogMissione> logs = logDAO.findByMissione(id);

            Utente currentUser = (Utente) session.getAttribute("currentUser");
            Map<String, Object> data = new HashMap<>();
            data.put("title", "Dettaglio Missione #" + id);
            data.put("user", currentUser);
            data.put("contextPath", request.getContextPath());
            data.put("missione", missione);
            data.put("logs", logs);

            processTemplate("dettaglio_missione.ftl", data, request, response);
            
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/missioni");
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

        String action = request.getParameter("action");
        String idStr = request.getParameter("id");

        if (idStr == null || idStr.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/missioni");
            return;
        }

        try {
            Integer id = Integer.parseInt(idStr);
            MissioneDAO missioneDAO = DAOFactory.getMissioneDAO(request);
            Missione missione = missioneDAO.findById(id);

            if (missione == null) {
                renderErrorPage(request, response, "Missione non trovata", 404);
                return;
            }

            Utente currentUser = (Utente) session.getAttribute("currentUser");

            if (currentUser.getRuolo() == Ruolo.OPERATORE) {
                boolean assegnato = missione.getCaposquadra().equals(currentUser) || missione.getOperatori().contains(currentUser);
                if (!assegnato) {
                    renderErrorPage(request, response, "Accesso negato: non sei assegnato a questa missione.", 403);
                    return;
                }
            }

            if ("addLog".equals(action)) {
                String descrizione = request.getParameter("descrizione");
                if (descrizione != null && !descrizione.trim().isEmpty()) {
                    LogMissione log = new LogMissione(missione, currentUser, LocalDateTime.now(), descrizione);
                    LogMissioneDAO logDAO = DAOFactory.getLogMissioneDAO(request);
                    logDAO.save(log);
                }
            } else if ("closeMission".equals(action)) {
                if (currentUser.getRuolo() != Ruolo.ADMIN) {
                    renderErrorPage(request, response, "Accesso negato: solo gli amministratori possono chiudere le missioni.", 403);
                    return;
                }
                
                missione.setStato(StatoMissione.COMPLETATA);
                missione.setDataOraFine(LocalDateTime.now());
                missioneDAO.save(missione);
                
                MezzoDAO mezzoDAO = DAOFactory.getMezzoDAO(request);
                for (Mezzo m : missione.getMezzi()) {
                    m.setDisponibilita(StatoMezzo.SI);
                    mezzoDAO.save(m);
                }
                
                LogMissione finalLog = new LogMissione(missione, currentUser, LocalDateTime.now(), "Missione chiusa con successo.");
                LogMissioneDAO logDAO = DAOFactory.getLogMissioneDAO(request);
                logDAO.save(finalLog);
                
                EmailService.notificaChiusuraMissione(missione);
            }

            response.sendRedirect(request.getContextPath() + "/dettaglio-missione?id=" + id);

        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/missioni");
        }
    }
}
