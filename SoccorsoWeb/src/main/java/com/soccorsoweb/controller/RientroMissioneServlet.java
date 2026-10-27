package com.soccorsoweb.controller;

import com.soccorsoweb.data.DAOFactory;
import com.soccorsoweb.data.MaterialeDAO;
import com.soccorsoweb.data.MezzoDAO;
import com.soccorsoweb.data.MissioneDAO;
import com.soccorsoweb.data.LogMissioneDAO;
import com.soccorsoweb.model.LogMissione;
import com.soccorsoweb.model.Materiale;
import com.soccorsoweb.model.Mezzo;
import com.soccorsoweb.model.Missione;
import com.soccorsoweb.model.Utente;
import com.soccorsoweb.model.enums.Ruolo;
import com.soccorsoweb.model.enums.StatoMezzo;
import com.soccorsoweb.model.enums.StatoMissione;
import com.soccorsoweb.model.enums.StatoRichiesta;
import com.soccorsoweb.util.EmailService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "RientroMissioneServlet", urlPatterns = { "/rientro-missione" })
public class RientroMissioneServlet extends BaseServlet {

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
            Missione missione = missioneDAO.findById(id);

            if (missione == null || missione.getStato() != StatoMissione.ATTIVA) {
                response.sendRedirect(request.getContextPath() + "/missioni");
                return;
            }

            Utente currentUser = (Utente) session.getAttribute("currentUser");

            boolean isCaposquadra = missione.getCaposquadra() != null
                    && missione.getCaposquadra().getId().equals(currentUser.getId());
            if (currentUser.getRuolo() != Ruolo.ADMIN && !isCaposquadra) {
                renderErrorPage(request, response, "Accesso negato: non hai i permessi per chiudere questa missione.",
                        403);
                return;
            }

            Map<String, Object> data = new HashMap<>();
            data.put("title", "Rientro Risorse - Missione #" + id);
            data.put("user", currentUser);
            data.put("contextPath", request.getContextPath());
            data.put("missione", missione);

            processTemplate("rientro_missione.ftl", data, request, response);

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
                response.sendRedirect(request.getContextPath() + "/missioni");
                return;
            }

            Utente currentUser = (Utente) session.getAttribute("currentUser");

            boolean isCaposquadra = missione.getCaposquadra() != null
                    && missione.getCaposquadra().getId().equals(currentUser.getId());
            if (currentUser.getRuolo() != Ruolo.ADMIN && !isCaposquadra) {
                renderErrorPage(request, response, "Accesso negato: non hai i permessi per chiudere questa missione.",
                        403);
                return;
            }

            MaterialeDAO materialeDAO = DAOFactory.getMaterialeDAO(request);
            StringBuilder logRientro = new StringBuilder("Riconsegna materiali: ");
            boolean materialiRiconsegnati = false;

            for (Materiale mat : missione.getMateriali()) {
                String qtaRestituitaStr = request.getParameter("restituisci_mat_" + mat.getId());
                if (qtaRestituitaStr != null && !qtaRestituitaStr.isEmpty()) {
                    int qtaRestituita = Integer.parseInt(qtaRestituitaStr);
                    if (qtaRestituita > 0) {
                        mat.setQuantitaDisponibile(mat.getQuantitaDisponibile() + qtaRestituita);
                        materialeDAO.save(mat);
                        logRientro.append("\n- ").append(mat.getTipo()).append(": restituiti ").append(qtaRestituita);
                        materialiRiconsegnati = true;
                    }
                }
            }

            MezzoDAO mezzoDAO = DAOFactory.getMezzoDAO(request);
            for (Mezzo m : missione.getMezzi()) {
                m.setDisponibilita(StatoMezzo.SI);
                mezzoDAO.save(m);
            }

            String valutazioneStr = request.getParameter("valutazione");
            if (valutazioneStr != null && !valutazioneStr.isEmpty()) {
                missione.setLivelloSuccesso(Integer.parseInt(valutazioneStr));
            }

            missione.setStato(StatoMissione.COMPLETATA);
            missione.setDataOraFine(LocalDateTime.now());

            if (missione.getRichiesta() != null) {
                missione.getRichiesta().setStato(StatoRichiesta.CHIUSA);
                DAOFactory.getRichiestaSoccorsoDAO(request).save(missione.getRichiesta());
            }

            missioneDAO.save(missione);

            LogMissioneDAO logDAO = DAOFactory.getLogMissioneDAO(request);
            String descLog = "Missione chiusa con valutazione "
                    + (missione.getLivelloSuccesso() != null ? missione.getLivelloSuccesso() : "N/D") + "/5. "
                    + (materialiRiconsegnati ? logRientro.toString() : "Nessun materiale riconsegnato.");
            LogMissione finalLog = new LogMissione(missione, currentUser, LocalDateTime.now(), descLog);
            logDAO.save(finalLog);

            EmailService.notificaChiusuraMissione(missione);

            response.sendRedirect(request.getContextPath() + "/missioni");

        } catch (Exception e) {
            response.sendRedirect(request.getContextPath() + "/missioni");
        }
    }
}
