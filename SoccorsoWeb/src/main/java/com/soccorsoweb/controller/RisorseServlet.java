package com.soccorsoweb.controller;

import com.soccorsoweb.data.DAOFactory;
import com.soccorsoweb.data.MaterialeDAO;
import com.soccorsoweb.data.MezzoDAO;
import com.soccorsoweb.data.UtenteDAO;
import com.soccorsoweb.model.Materiale;
import com.soccorsoweb.model.Mezzo;
import com.soccorsoweb.model.Utente;
import com.soccorsoweb.model.enums.Ruolo;
import com.soccorsoweb.model.enums.StatoMezzo;
import com.soccorsoweb.security.Hashing;
import com.soccorsoweb.util.EmailService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "RisorseServlet", urlPatterns = { "/gestione-risorse" })
public class RisorseServlet extends BaseServlet {

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
            renderErrorPage(request, response, "Accesso non autorizzato", "Solo gli amministratori possono gestire le risorse.", 403);
            return;
        }

        Map<String, Object> data = new HashMap<>();
        data.put("title", "Gestione Risorse");
        data.put("user", currentUser);
        data.put("contextPath", request.getContextPath());


        UtenteDAO utenteDAO = DAOFactory.getUtenteDAO(request);
        List<Utente> utenti = utenteDAO.findAll();
        data.put("utenti", utenti);


        MezzoDAO mezzoDAO = DAOFactory.getMezzoDAO(request);
        List<Mezzo> mezzi = mezzoDAO.findAll();
        data.put("mezzi", mezzi);

  
        MaterialeDAO materialeDAO = DAOFactory.getMaterialeDAO(request);
        List<Materiale> materiali = materialeDAO.findAll();
        data.put("materiali", materiali);


        data.put("tutteLePatenti", DAOFactory.getPatenteDAO(request).findAll());
        data.put("tutteLeSkill", DAOFactory.getSkillDAO(request).findAll());

        processTemplate("gestione_risorse.ftl", data, request, response);
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
        if (action == null) {
            response.sendRedirect(request.getContextPath() + "/gestione-risorse");
            return;
        }

        try {
            switch (action) {
                case "addOperatore":
                    handleAddOperatore(request);
                    break;
                case "addMezzo":
                    handleAddMezzo(request);
                    break;
                case "addMateriale":
                    handleAddMateriale(request);
                    break;
                case "deleteOperatore":
                    handleDeleteOperatore(request);
                    break;
                case "deleteMezzo":
                    handleDeleteMezzo(request);
                    break;
                case "deleteMateriale":
                    handleDeleteMateriale(request);
                    break;
                case "addPatente":
                    handleAddPatente(request);
                    break;
                case "addSkill":
                    handleAddSkill(request);
                    break;
                case "deletePatente":
                    handleDeletePatente(request);
                    break;
                case "deleteSkill":
                    handleDeleteSkill(request);
                    break;
            }
        } catch (Exception e) {
            request.setAttribute("errorMessage", "Errore durante l'operazione: " + e.getMessage());
        }

        doGet(request, response);
    }

    private void handleDeletePatente(HttpServletRequest request) {
        String idStr = request.getParameter("id");
        if (idStr != null) {
            try {
                DAOFactory.getPatenteDAO(request).deleteById(Integer.parseInt(idStr));
                request.getSession().setAttribute("successMessage", "Patente rimossa dal sistema.");
            } catch (Exception e) {
                request.getSession().setAttribute("errorMessage", "Impossibile eliminare la patente: è probabilmente assegnata a degli utenti.");
            }
        }
    }

    private void handleDeleteSkill(HttpServletRequest request) {
        String idStr = request.getParameter("id");
        if (idStr != null) {
            try {
                DAOFactory.getSkillDAO(request).deleteById(Integer.parseInt(idStr));
                request.getSession().setAttribute("successMessage", "Abilità/Skill rimossa dal sistema.");
            } catch (Exception e) {
                request.getSession().setAttribute("errorMessage", "Impossibile eliminare la skill: è probabilmente assegnata a degli utenti.");
            }
        }
    }

    private void handleAddPatente(HttpServletRequest request) {
        String tipologia = request.getParameter("tipologia");
        if (tipologia != null && !tipologia.trim().isEmpty()) {
            com.soccorsoweb.model.Patente p = new com.soccorsoweb.model.Patente(tipologia);
            DAOFactory.getPatenteDAO(request).save(p);
            request.getSession().setAttribute("successMessage", "Nuova tipologia di patente aggiunta!");
        }
    }

    private void handleAddSkill(HttpServletRequest request) {
        String descrizione = request.getParameter("descrizione");
        if (descrizione != null && !descrizione.trim().isEmpty()) {
            com.soccorsoweb.model.Skill s = new com.soccorsoweb.model.Skill(descrizione);
            DAOFactory.getSkillDAO(request).save(s);
            request.getSession().setAttribute("successMessage", "Nuova abilità/skill aggiunta al sistema!");
        }
    }

    private void handleAddOperatore(HttpServletRequest request) {
        String nome = request.getParameter("nome");
        String cognome = request.getParameter("cognome");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String passwordConfirm = request.getParameter("password_confirm");
        String ruoloStr = request.getParameter("ruolo");

        if (nome != null && cognome != null && email != null && password != null && passwordConfirm != null && ruoloStr != null) {
            
     
            if (!password.equals(passwordConfirm)) {
                request.setAttribute("errorMessage", "Errore: Le password non coincidono.");
                return;
            }

            UtenteDAO utenteDAO = DAOFactory.getUtenteDAO(request);
            
      
            if (utenteDAO.findByEmail(email) != null) {
                request.setAttribute("errorMessage", "Errore: Esiste già un utente con questa email.");
                return;
            }

            Utente nuovoOperatore = new Utente();
            nuovoOperatore.setNome(nome);
            nuovoOperatore.setCognome(cognome);
            nuovoOperatore.setEmail(email);
            nuovoOperatore.setPassword(Hashing.hashPassword(password));
            nuovoOperatore.setRuolo(Ruolo.valueOf(ruoloStr));

         
            String[] patentiIds = request.getParameterValues("patentiIds");
            if (patentiIds != null) {
                for (String pId : patentiIds) {
                    nuovoOperatore.getPatenti().add(DAOFactory.getPatenteDAO(request).findById(Integer.parseInt(pId)));
                }
            }

            String[] skillsIds = request.getParameterValues("skillsIds");
            if (skillsIds != null) {
                for (String sId : skillsIds) {
                    nuovoOperatore.getSkills().add(DAOFactory.getSkillDAO(request).findById(Integer.parseInt(sId)));
                }
            }

            utenteDAO.save(nuovoOperatore);
            
  
            EmailService.inviaCredenzialiUtente(nuovoOperatore, password);
            
            request.getSession().setAttribute("successMessage", "Utente aggiunto con successo. Credenziali inviate via email a " + email);
        }
    }

    private void handleAddMezzo(HttpServletRequest request) {
        String tipo = request.getParameter("tipo");
        String descrizione = request.getParameter("descrizione");

        if (tipo != null && !tipo.trim().isEmpty()) {
            Mezzo nuovoMezzo = new Mezzo();
            nuovoMezzo.setTipo(tipo);
            nuovoMezzo.setDescrizione(descrizione);
            nuovoMezzo.setDisponibilita(StatoMezzo.SI);

            MezzoDAO mezzoDAO = DAOFactory.getMezzoDAO(request);
            mezzoDAO.save(nuovoMezzo);
            request.setAttribute("successMessage", "Mezzo aggiunto con successo!");
        }
    }

    private void handleAddMateriale(HttpServletRequest request) {
        String tipo = request.getParameter("tipo");
        String descrizione = request.getParameter("descrizione");
        String quantitaStr = request.getParameter("quantita");

        if (tipo != null && !tipo.trim().isEmpty() && quantitaStr != null) {
            Materiale nuovoMateriale = new Materiale();
            nuovoMateriale.setTipo(tipo);
            nuovoMateriale.setDescrizione(descrizione);
            nuovoMateriale.setQuantitaDisponibile(Integer.parseInt(quantitaStr));

            MaterialeDAO materialeDAO = DAOFactory.getMaterialeDAO(request);
            materialeDAO.save(nuovoMateriale);
            request.setAttribute("successMessage", "Materiale aggiunto con successo!");
        }
    }

    private void handleDeleteOperatore(HttpServletRequest request) {
        String idStr = request.getParameter("id");
        if (idStr != null) {
            try {
                UtenteDAO utenteDAO = DAOFactory.getUtenteDAO(request);
                utenteDAO.deleteById(Integer.parseInt(idStr));
                request.setAttribute("successMessage", "Operatore rimosso con successo.");
            } catch (Exception e) {
                request.setAttribute("errorMessage", "Impossibile eliminare l'operatore: è probabilmente legato a missioni esistenti.");
            }
        }
    }

    private void handleDeleteMezzo(HttpServletRequest request) {
        String idStr = request.getParameter("id");
        if (idStr != null) {
            try {
                MezzoDAO mezzoDAO = DAOFactory.getMezzoDAO(request);
                mezzoDAO.deleteById(Integer.parseInt(idStr));
                request.setAttribute("successMessage", "Mezzo rimosso con successo.");
            } catch (Exception e) {
                request.setAttribute("errorMessage", "Impossibile eliminare il mezzo: è legato a uno storico missioni.");
            }
        }
    }

    private void handleDeleteMateriale(HttpServletRequest request) {
        String idStr = request.getParameter("id");
        if (idStr != null) {
            try {
                MaterialeDAO materialeDAO = DAOFactory.getMaterialeDAO(request);
                materialeDAO.deleteById(Integer.parseInt(idStr));
                request.setAttribute("successMessage", "Materiale rimosso con successo.");
            } catch (Exception e) {
                request.setAttribute("errorMessage", "Impossibile eliminare il materiale: è legato a missioni passate.");
            }
        }
    }
}
