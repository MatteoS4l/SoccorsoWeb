package com.soccorsoweb.controller;

import com.soccorsoweb.data.UtenteDAO;
import com.soccorsoweb.data.DAOFactory;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.soccorsoweb.model.Utente;
import com.soccorsoweb.model.enums.Ruolo;
import jakarta.servlet.annotation.WebServlet;

@WebServlet(name = "LoginServlet", urlPatterns = { "/login" })
public class LoginServlet extends BaseServlet {



    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Map<String, Object> data = new HashMap<>();

        String loginError = (String) request.getSession().getAttribute("loginError");
        if (loginError != null) {
            data.put("loginError", loginError);
            request.getSession().removeAttribute("loginError"); // Clear the error after displaying
        }
        processTemplate("login.ftl", data, request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        if (email == null || email.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            request.getSession().setAttribute("loginError", "Email e password sono campi obbligatori.");
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        try {

            UtenteDAO utenteDAO = DAOFactory.getUtenteDAO(request);
            
            Utente user = utenteDAO.checkLogin(email, password);
            if (user != null) {
                HttpSession session = request.getSession();
                session.setAttribute("currentUser", user);
                
                if (user.getRuolo() == Ruolo.ADMIN) {
                    response.sendRedirect(request.getContextPath() + "/dashboard");
                } else {
                    response.sendRedirect(request.getContextPath() + "/missioni");
                }
            } else {
                request.getSession().setAttribute("loginError", "Credenziali non valide. Riprova.");
                response.sendRedirect(request.getContextPath() + "/login");
            }
        } catch (Exception e) {
            request.getSession().setAttribute("loginError",
                    "Si è verificato un errore durante il login. Riprova più tardi.");
            response.sendRedirect(request.getContextPath() + "/login");
        }
    }
}