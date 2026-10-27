package com.soccorsoweb.controller;

import com.soccorsoweb.data.DAOFactory;
import com.soccorsoweb.data.MezzoDAO;
import com.soccorsoweb.data.MissioneDAO;
import com.soccorsoweb.data.RichiestaSoccorsoDAO;
import com.soccorsoweb.data.UtenteDAO;
import com.soccorsoweb.data.MaterialeDAO;
import com.soccorsoweb.model.Materiale;
import com.soccorsoweb.model.Mezzo;
import com.soccorsoweb.model.Missione;
import com.soccorsoweb.model.RichiestaSoccorso;
import com.soccorsoweb.model.Utente;
import com.soccorsoweb.model.enums.Ruolo;
import com.soccorsoweb.model.enums.StatoMezzo;
import com.soccorsoweb.model.enums.StatoMissione;
import com.soccorsoweb.model.enums.StatoRichiesta;
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
import com.soccorsoweb.data.LogMissioneDAO;
import com.soccorsoweb.model.LogMissione;
import com.soccorsoweb.util.EmailService;

@WebServlet(name = "NuovaMissioneServlet", urlPatterns = { "/nuova-missione" })
public class NuovaMissioneServlet extends BaseServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("currentUser") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        Utente admin = (Utente) session.getAttribute("currentUser");
        if (!admin.getRuolo().equals(Ruolo.ADMIN)) {
            response.sendRedirect(request.getContextPath() + "/dashboard");
            return;
        }

        String richiestaIdStr = request.getParameter("richiestaId");
        if (richiestaIdStr == null || richiestaIdStr.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/dashboard");
            return;
        }

        try {
            Integer richiestaId = Integer.parseInt(richiestaIdStr);
            RichiestaSoccorsoDAO richiestaDAO = DAOFactory.getRichiestaSoccorsoDAO(request);
            RichiestaSoccorso richiesta = richiestaDAO.findById(richiestaId);

            if (richiesta == null) {
                response.sendRedirect(request.getContextPath() + "/dashboard");
                return;
            }

            UtenteDAO utenteDAO = DAOFactory.getUtenteDAO(request);
            List<Utente> operatoriDisponibili = utenteDAO.findOperatoriLiberi();

            MezzoDAO mezzoDAO = DAOFactory.getMezzoDAO(request);
            List<Mezzo> mezziDisponibili = mezzoDAO.findByStato(StatoMezzo.SI);

            MaterialeDAO materialeDAO = DAOFactory.getMaterialeDAO(request);
            List<Materiale> materiali = materialeDAO.findAll();

            Map<String, Object> data = new HashMap<>();
            data.put("title", "Nuova Missione");
            data.put("richiesta", richiesta);
            data.put("operatoriDisponibili", operatoriDisponibili);
            data.put("mezziDisponibili", mezziDisponibili);
            data.put("materiali", materiali);
            data.put("contextPath", request.getContextPath());
            data.put("user", session.getAttribute("currentUser"));

            processTemplate("nuova_missione.ftl", data, request, response);

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

        Utente admin = (Utente) session.getAttribute("currentUser");
        if (!admin.getRuolo().equals(Ruolo.ADMIN)) {
            response.sendRedirect(request.getContextPath() + "/dashboard");
            return;
        }

        String richiestaIdStr = request.getParameter("richiestaId");
        String obiettivo = request.getParameter("obiettivo");
        String note = request.getParameter("note");

        String caposquadraIdStr = request.getParameter("caposquadraId");
        String[] operatoriIdsStr = request.getParameterValues("operatoriIds");
        String[] mezziIdsStr = request.getParameterValues("mezziIds");

        if (richiestaIdStr == null || obiettivo == null || obiettivo.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/dashboard");
            return;
        }

        try {
            Integer richiestaId = Integer.parseInt(richiestaIdStr);
            RichiestaSoccorsoDAO richiestaDAO = DAOFactory.getRichiestaSoccorsoDAO(request);
            RichiestaSoccorso richiesta = richiestaDAO.findById(richiestaId);

            UtenteDAO utenteDAO = DAOFactory.getUtenteDAO(request);
            MezzoDAO mezzoDAO = DAOFactory.getMezzoDAO(request);

            if (richiesta == null) {
                response.sendRedirect(request.getContextPath() + "/dashboard");
                return;
            }

            richiesta.setStato(StatoRichiesta.CONFERMATA);
            richiestaDAO.save(richiesta);

            Missione missione = new Missione();
            missione.setRichiesta(richiesta);
            missione.setObiettivo(obiettivo);
            missione.setNote(note);
            missione.setDataOraInizio(LocalDateTime.now());
            missione.setStato(StatoMissione.ATTIVA);

            missione.setAdmin(admin);

            if (caposquadraIdStr != null && !caposquadraIdStr.isEmpty()) {
                Utente caposquadra = utenteDAO.findById(Integer.parseInt(caposquadraIdStr));
                missione.setCaposquadra(caposquadra);
            }

            if (operatoriIdsStr != null) {
                for (String opIdStr : operatoriIdsStr) {
                    Utente op = utenteDAO.findById(Integer.parseInt(opIdStr));
                    if (op != null) {
                        missione.getOperatori().add(op);
                    }
                }
            }

            if (mezziIdsStr != null) {
                for (String mezzoIdStr : mezziIdsStr) {
                    Mezzo mezzo = mezzoDAO.findById(Integer.parseInt(mezzoIdStr));
                    if (mezzo != null) {
                        missione.getMezzi().add(mezzo);
                        mezzo.setDisponibilita(StatoMezzo.NO);
                        mezzoDAO.save(mezzo);
                    }
                }
            }

            MaterialeDAO materialeDAO = DAOFactory.getMaterialeDAO(request);
            List<Materiale> tuttiMateriali = materialeDAO.findAll();
            StringBuilder logMateriali = new StringBuilder("Materiali prelevati dal magazzino: ");
            boolean materialiAggiunti = false;

            for (Materiale mat : tuttiMateriali) {
                String qtaStr = request.getParameter("quantita_mat_" + mat.getId());
                if (qtaStr != null && !qtaStr.isEmpty()) {
                    int qtaRichiesta = Integer.parseInt(qtaStr);
                    if (qtaRichiesta > 0) {
                        if (qtaRichiesta > mat.getQuantitaDisponibile()) {
                            throw new Exception("Quantità insufficiente per " + mat.getTipo() + " (Disponibile: "
                                    + mat.getQuantitaDisponibile() + ")");
                        }

                        mat.setQuantitaDisponibile(mat.getQuantitaDisponibile() - qtaRichiesta);
                        materialeDAO.save(mat);

                        missione.getMateriali().add(mat);

                        logMateriali.append("\n- ").append(mat.getTipo()).append(": ").append(qtaRichiesta);
                        materialiAggiunti = true;
                    }
                }
            }

            MissioneDAO missioneDAO = DAOFactory.getMissioneDAO(request);
            missione = missioneDAO.save(missione);

            LogMissioneDAO logDAO = DAOFactory.getLogMissioneDAO(request);
            String descLog = "Missione avviata. "
                    + (materialiAggiunti ? logMateriali.toString() : "Nessun materiale prelevato.");
            LogMissione initialLog = new LogMissione(missione, admin, LocalDateTime.now(), descLog);
            logDAO.save(initialLog);

            EmailService.avvisaOperatoriMissione(missione);

            response.sendRedirect(request.getContextPath() + "/missioni");

        } catch (Exception e) {

            response.sendRedirect(request.getContextPath() + "/dashboard");
        }
    }
}
