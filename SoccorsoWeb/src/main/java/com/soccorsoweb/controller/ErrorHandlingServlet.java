package com.soccorsoweb.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "ErrorHandlingServlet", urlPatterns = { "/error-handler" })
public class ErrorHandlingServlet extends BaseServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        Integer statusCode = (Integer) request.getAttribute("jakarta.servlet.error.status_code");
        String message;
        String title;

        if (statusCode == null) {
            statusCode = 500;
        }

        switch (statusCode) {
            case 404:
                title = "Pagina Non Trovata";
                message = "La risorsa che stai cercando non esiste o è stata spostata.";
                break;
            case 403:
                title = "Accesso non autorizzato";
                message = "Non hai i permessi necessari per accedere a questa risorsa.";
                break;
            default:
                title = "Sistema in Anomalia";
                message = "Si è verificato un errore interno al server durante l'elaborazione.";
                break;
        }

        renderErrorPage(request, response, title, message, statusCode);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
