package com.soccorsoweb.controller;

import com.soccorsoweb.data.DAOFactory;
import com.soccorsoweb.data.UtenteDAO;
import com.soccorsoweb.model.Utente;
import com.soccorsoweb.security.Hashing;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "ProfiloServlet", urlPatterns = { "/profilo" })
public class ProfiloServlet extends BaseServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("currentUser") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        Utente currentUser = (Utente) session.getAttribute("currentUser");
        UtenteDAO utenteDAO = DAOFactory.getUtenteDAO(request);
        
        Utente profileUser = utenteDAO.findById(currentUser.getId());
        
        Map<String, Object> data = new HashMap<>();
        data.put("title", "Mio Profilo");
        data.put("user", currentUser); 
        data.put("profileUser", profileUser);
        data.put("contextPath", request.getContextPath());
        data.put("activePage", "profilo");

        data.put("tutteLePatenti", DAOFactory.getPatenteDAO(request).findAll());
        data.put("tutteLeSkill", DAOFactory.getSkillDAO(request).findAll());

        processTemplate("profilo.ftl", data, request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("currentUser") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        Utente currentUser = (Utente) session.getAttribute("currentUser");
        UtenteDAO utenteDAO = DAOFactory.getUtenteDAO(request);

        String nome = request.getParameter("nome");
        String cognome = request.getParameter("cognome");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String passwordConfirm = request.getParameter("password_confirm");
        
        String[] patentiIds = request.getParameterValues("patentiIds");
        String[] skillsIds = request.getParameterValues("skillsIds");

        try {
            Utente u = utenteDAO.findById(currentUser.getId());
            
            u.setNome(nome);
            u.setCognome(cognome);
            u.setEmail(email);

            if (password != null && !password.isEmpty()) {
                if (!password.equals(passwordConfirm)) {
                    request.setAttribute("errorMessage", "Le password non coincidono.");
                    doGet(request, response);
                    return;
                }
                u.setPassword(Hashing.hashPassword(password));
            }

            u.getPatenti().clear();
            if (patentiIds != null) {
                for (String pId : patentiIds) {
                    u.getPatenti().add(DAOFactory.getPatenteDAO(request).findById(Integer.parseInt(pId)));
                }
            }

            u.getSkills().clear();
            if (skillsIds != null) {
                for (String sId : skillsIds) {
                    u.getSkills().add(DAOFactory.getSkillDAO(request).findById(Integer.parseInt(sId)));
                }
            }

            utenteDAO.save(u);
            
            session.setAttribute("currentUser", u);
            session.setAttribute("successMessage", "Profilo e competenze aggiornati con successo!");
            
            response.sendRedirect(request.getContextPath() + "/profilo");

        } catch (Exception e) {
            request.setAttribute("errorMessage", "Errore durante l'aggiornamento: " + e.getMessage());
            doGet(request, response);
        }
    }
}