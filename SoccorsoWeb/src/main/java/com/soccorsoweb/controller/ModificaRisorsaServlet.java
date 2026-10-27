package com.soccorsoweb.controller;

import com.soccorsoweb.data.DAOFactory;
import com.soccorsoweb.data.MaterialeDAO;
import com.soccorsoweb.data.MezzoDAO;
import com.soccorsoweb.data.UtenteDAO;
import com.soccorsoweb.model.Materiale;
import com.soccorsoweb.model.Mezzo;
import com.soccorsoweb.model.Utente;
import com.soccorsoweb.model.enums.Ruolo;
import com.soccorsoweb.util.EmailService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "ModificaRisorsaServlet", urlPatterns = { "/modifica-risorsa" })
public class ModificaRisorsaServlet extends BaseServlet {

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
            renderErrorPage(request, response, "Accesso non autorizzato",
                    "Solo gli amministratori possono modificare le risorse di sistema.", 403);
            return;
        }

        String type = request.getParameter("type");
        String idStr = request.getParameter("id");

        if (type == null || idStr == null) {
            response.sendRedirect(request.getContextPath() + "/gestione-risorse");
            return;
        }

        try {
            Integer id = Integer.parseInt(idStr);
            Map<String, Object> data = new HashMap<>();
            data.put("title", "Modifica " + type);
            data.put("user", currentUser);
            data.put("contextPath", request.getContextPath());
            data.put("type", type);

            data.put("tutteLePatenti", DAOFactory.getPatenteDAO(request).findAll());
            data.put("tutteLeSkill", DAOFactory.getSkillDAO(request).findAll());

            switch (type) {
                case "operatore":
                    data.put("risorsa", DAOFactory.getUtenteDAO(request).findById(id));
                    break;
                case "mezzo":
                    data.put("risorsa", DAOFactory.getMezzoDAO(request).findById(id));
                    break;
                case "materiale":
                    data.put("risorsa", DAOFactory.getMaterialeDAO(request).findById(id));
                    break;
            }

            processTemplate("modifica_risorsa.ftl", data, request, response);

        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/gestione-risorse");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String type = request.getParameter("type");
        String idStr = request.getParameter("id");

        try {
            Integer id = Integer.parseInt(idStr);
            if ("operatore".equals(type)) {
                UtenteDAO dao = DAOFactory.getUtenteDAO(request);
                Utente u;
                String password = request.getParameter("password");
                String passwordConfirm = request.getParameter("password_confirm");

                if (id == 0) {
                    u = new Utente();
                    if (password == null || password.isEmpty()) {
                        renderErrorPage(request, response, "La password è obbligatoria per i nuovi utenti.", 400);
                        return;
                    }
                    if (!password.equals(passwordConfirm)) {
                        renderErrorPage(request, response, "Le password non coincidono.", 400);
                        return;
                    }
                    u.setPassword(com.soccorsoweb.security.Hashing.hashPassword(password));
                    request.getSession().setAttribute("successMessage",
                            "Utente creato con successo! Email di benvenuto inviata a " + request.getParameter("email")
                                    + " con password: " + password);
                } else {
                    u = dao.findById(id);

                    if (password != null && !password.isEmpty()) {
                        if (!password.equals(passwordConfirm)) {
                            renderErrorPage(request, response, "Le password non coincidono.", 400);
                            return;
                        }
                        u.setPassword(com.soccorsoweb.security.Hashing.hashPassword(password));
                        request.getSession().setAttribute("successMessage",
                                "Dati utente e password aggiornati con successo!");
                    } else {
                        request.getSession().setAttribute("successMessage", "Dati utente aggiornati con successo!");
                    }
                }
                u.setNome(request.getParameter("nome"));
                u.setCognome(request.getParameter("cognome"));
                u.setEmail(request.getParameter("email"));
                u.setRuolo(Ruolo.valueOf(request.getParameter("ruolo")));

                u.getPatenti().clear();
                String[] patentiIds = request.getParameterValues("patentiIds");
                if (patentiIds != null) {
                    for (String pId : patentiIds) {
                        u.getPatenti().add(DAOFactory.getPatenteDAO(request).findById(Integer.parseInt(pId)));
                    }
                }

                u.getSkills().clear();
                String[] skillsIds = request.getParameterValues("skillsIds");
                if (skillsIds != null) {
                    for (String sId : skillsIds) {
                        u.getSkills().add(DAOFactory.getSkillDAO(request).findById(Integer.parseInt(sId)));
                    }
                }

                dao.save(u);

                if (id == 0) {
                    EmailService.inviaCredenzialiUtente(u, password);
                }
            } else if ("mezzo".equals(type)) {
                MezzoDAO dao = DAOFactory.getMezzoDAO(request);
                Mezzo m = (id == 0) ? new Mezzo() : dao.findById(id);
                m.setTipo(request.getParameter("tipo"));
                m.setDescrizione(request.getParameter("descrizione"));
                if (id == 0)
                    m.setDisponibilita(com.soccorsoweb.model.enums.StatoMezzo.SI);
                dao.save(m);
            } else if ("materiale".equals(type)) {
                MaterialeDAO dao = DAOFactory.getMaterialeDAO(request);
                Materiale mat = (id == 0) ? new Materiale() : dao.findById(id);
                mat.setTipo(request.getParameter("tipo"));
                mat.setDescrizione(request.getParameter("descrizione"));
                mat.setQuantitaDisponibile(Integer.parseInt(request.getParameter("quantita")));
                dao.save(mat);
            }

            response.sendRedirect(request.getContextPath() + "/gestione-risorse");
        } catch (Exception e) {
            renderErrorPage(request, response, "Errore durante il salvataggio: " + e.getMessage(), 500);
        }
    }
}
