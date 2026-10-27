package com.soccorsoweb.controller;

import com.soccorsoweb.util.FreeMarkerTemplateManager;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

// La base servlet serve per le funzionalità del rendering dei template FreeMarker 

public abstract class BaseServlet extends HttpServlet {

    protected void processTemplate(String templateName, Map<String, Object> dataModel,
            HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        try {
            // Aggiungo il contextPath al dataModel se non presente
            if (dataModel == null) {
                dataModel = new HashMap<>();
            }
            dataModel.put("contextPath", request.getContextPath());

            if (request.getAttribute("successMessage") != null) {
                dataModel.put("successMessage", request.getAttribute("successMessage"));
            }
            if (request.getAttribute("errorMessage") != null) {
                dataModel.put("errorMessage", request.getAttribute("errorMessage"));
            }

            jakarta.servlet.http.HttpSession session = request.getSession(false);
            if (session != null) {
                if (session.getAttribute("successMessage") != null) {
                    dataModel.put("successMessage", session.getAttribute("successMessage"));
                    session.removeAttribute("successMessage");
                }
                if (session.getAttribute("errorMessage") != null) {
                    dataModel.put("errorMessage", session.getAttribute("errorMessage"));
                    session.removeAttribute("errorMessage");
                }
            }

            Template template = FreeMarkerTemplateManager.getConfiguration().getTemplate(templateName);
            response.setContentType("text/html;charset=UTF-8");
            template.process(dataModel, response.getWriter());
        } catch (TemplateException e) {

            throw new ServletException("Error processing template: " + templateName, e);
        }
    }

    protected void renderErrorPage(HttpServletRequest request, HttpServletResponse response,
            String message, int statusCode) throws IOException, ServletException {
        renderErrorPage(request, response, "Sistema in Anomalia", message, statusCode);
    }

    protected void renderErrorPage(HttpServletRequest request, HttpServletResponse response,
            String title, String message, int statusCode) throws IOException, ServletException {
        response.setStatus(statusCode);
        Map<String, Object> dataModel = new HashMap<>();
        dataModel.put("errorTitle", title);
        dataModel.put("errorMessage", message);
        processTemplate("error.ftl", dataModel, request, response);
    }
}
