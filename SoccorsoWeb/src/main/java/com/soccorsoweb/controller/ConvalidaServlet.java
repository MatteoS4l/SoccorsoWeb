package com.soccorsoweb.controller;

import com.soccorsoweb.data.DAOFactory;
import com.soccorsoweb.data.RichiestaSoccorsoDAO;
import com.soccorsoweb.model.RichiestaSoccorso;
import com.soccorsoweb.model.enums.StatoRichiesta;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "ConvalidaServlet", urlPatterns = { "/convalida" })
public class ConvalidaServlet extends BaseServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String token = request.getParameter("token");
        Map<String, Object> data = new HashMap<>();
        data.put("contextPath", request.getContextPath());

        if (token == null || token.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/");
            return;
        }

        RichiestaSoccorsoDAO richiestaDAO = DAOFactory.getRichiestaSoccorsoDAO(request);
        RichiestaSoccorso richiesta = richiestaDAO.findByCodiceConvalida(token);

        if (richiesta != null && richiesta.getStato() == StatoRichiesta.DA_CONVALIDARE) {

            richiesta.setStato(StatoRichiesta.IN_ATTESA);

            
            richiestaDAO.save(richiesta);
            
            processTemplate("convalida_successo.ftl", data, request, response);
        } else {
            data.put("errorMessage", "Il link di convalida non è valido o è già stato utilizzato.");
            processTemplate("error.ftl", data, request, response);
        }
    }
}
