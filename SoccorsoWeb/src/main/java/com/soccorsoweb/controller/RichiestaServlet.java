package com.soccorsoweb.controller;

import com.soccorsoweb.data.DAOFactory;
import com.soccorsoweb.data.RichiestaSoccorsoDAO;
import com.soccorsoweb.model.RichiestaSoccorso;
import com.soccorsoweb.model.enums.Priorita;
import com.soccorsoweb.model.enums.StatoRichiesta;
import com.soccorsoweb.model.enums.Tipologia;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.Part;
import java.nio.file.Paths;
import java.util.Random;
import java.util.UUID;
import com.soccorsoweb.util.EmailService;

@WebServlet(name = "RichiestaServlet", urlPatterns = { "/richiesta" })
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 2, // 2MB
        maxFileSize = 1024 * 1024 * 10, // 10MB
        maxRequestSize = 1024 * 1024 * 50 // 50MB
)
public class RichiestaServlet extends BaseServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        renderForm(request, response, new HashMap<>());
    }


    private void renderForm(HttpServletRequest request, HttpServletResponse response, Map<String, Object> data)
            throws ServletException, IOException {

// captcha
        Random ran = new Random();
        int n1 = ran.nextInt(10) + 1;
        int n2 = ran.nextInt(10) + 1;
        request.getSession().setAttribute("captcha_sum", n1 + n2);

        data.put("n1", n1);
        data.put("n2", n2);
        data.put("contextPath", request.getContextPath());
        data.put("tipologie", Tipologia.values());
        data.put("priorita", Priorita.values());
        processTemplate("richiesta.ftl", data, request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Map<String, Object> data = new HashMap<>();

        String nomeRichiedente = request.getParameter("nome_richiedente");
        String email = request.getParameter("email");
        String indirizzo = request.getParameter("indirizzo");
        String descrizione = request.getParameter("descrizione");
        String tipologiaStr = request.getParameter("tipologia");
        String prioritaStr = request.getParameter("priorita");
        String captchaRes = request.getParameter("captcha_result");

  
        Integer expectedSum = (Integer) request.getSession().getAttribute("captcha_sum");
        if (captchaRes == null || expectedSum == null || !captchaRes.equals(expectedSum.toString())) {
            data.put("errorMessage", "Errore, Captcha errato");
            renderForm(request, response, data);
            return;
        }


        if (nomeRichiedente == null || nomeRichiedente.isEmpty() ||
                email == null || email.isEmpty() ||
                indirizzo == null || indirizzo.isEmpty() ||
                descrizione == null || descrizione.isEmpty()) {

            data.put("errorMessage", "Tutti i campi obbligatori devono essere compilati.");
            renderForm(request, response, data);
            return;
        }


        RichiestaSoccorsoDAO richiestaDAO = DAOFactory.getRichiestaSoccorsoDAO(request);
        if (richiestaDAO.countRecentRequests(email, 10) > 0) {
            data.put("errorMessage", "Hai già inviato una richiesta recentemente. Per favore, attendi qualche minuto.");
            renderForm(request, response, data);
            return;
        }


        try {
            String fotoPath = null;
            Part filePart = request.getPart("foto");
            if (filePart != null && filePart.getSize() > 0) {
                String fileName = UUID.randomUUID().toString() + "_"
                        + Paths.get(filePart.getSubmittedFileName()).getFileName().toString();

                // Percorso relativo al contesto del server per garantire il funzionamento
                String uploadPath = getServletContext().getRealPath("/uploads");

                java.io.File uploadDir = new java.io.File(uploadPath);
                if (!uploadDir.exists()) {
                    uploadDir.mkdirs();
                }

                filePart.write(uploadPath + java.io.File.separator + fileName);
                fotoPath = "uploads/" + fileName;
            }

  
            RichiestaSoccorso richiesta = new RichiestaSoccorso();
            richiesta.setNomeRichiedente(nomeRichiedente);
            richiesta.setContattoEmail(email);
            richiesta.setIndirizzo(indirizzo);
            richiesta.setDescrizione(descrizione);
            richiesta.setFoto(fotoPath);
            richiesta.setCodiceConvalida(EmailService.generaCodiceConvalida());
            richiesta.setTipologia(Tipologia.valueOf(tipologiaStr));
            richiesta.setPriorita(Priorita.valueOf(prioritaStr));
            richiesta.setDataOraRichiesta(LocalDateTime.now());
            richiesta.setStato(StatoRichiesta.DA_CONVALIDARE);

            richiestaDAO.save(richiesta);

         
            EmailService.inviaConfermaRichiesta(richiesta, request.getContextPath(), request.getServerName(),
                    request.getServerPort());

            data.put("successMessage", "Richiesta inviata con successo! Controlla la tua email ("
                    + richiesta.getContattoEmail() + ") per attivare la segnalazione.");
            renderForm(request, response, data);

        } catch (Exception e) {
            data.put("errorMessage", "Errore tecnico durante l'invio. Riprova più tardi.");
            renderForm(request, response, data);
        }
    }
}