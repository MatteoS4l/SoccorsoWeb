package com.soccorsoweb.rest.resources;

import com.soccorsoweb.data.DAOFactory;
import com.soccorsoweb.data.UtenteDAO;
import com.soccorsoweb.model.Utente;
import com.soccorsoweb.rest.schemas.AutenticazioneSchema;
import com.soccorsoweb.rest.schemas.UtenteSchema;
import com.soccorsoweb.rest.security.Logged;
import com.soccorsoweb.rest.security.TokenManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/auth")
public class AutenticazioneResource {

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(@Context HttpServletRequest request, AutenticazioneSchema richiesta) {

        if (richiesta == null || richiesta.getEmail() == null || richiesta.getPassword() == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\":\"Email e password richiesti\"}").build();
        }

        // prende em e instanzia DAO
        UtenteDAO utenteDAO = DAOFactory.getUtenteDAO(request);
        Utente utente = utenteDAO.checkLogin(richiesta.getEmail(), richiesta.getPassword());

        if (utente != null) {
            // token random tramite TokenManager
            String token = TokenManager.generateToken(utente);

            UtenteSchema utenteSchema = new UtenteSchema(
                    utente.getId(),
                    utente.getNome(),
                    utente.getCognome(),
                    utente.getEmail(),
                    utente.getRuolo().name());

            AutenticazioneSchema risposta = new AutenticazioneSchema(token, utenteSchema);
            return Response.ok(risposta).build();
        } else {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("{\"error\":\"Credenziali non valide\"}").build();
        }
    }

    @Logged
    @POST
    @Path("/logout")
    @Produces(MediaType.APPLICATION_JSON)
    public Response logout(@Context ContainerRequestContext crc) {
        // recupero token validato dal context
        String token = (String) crc.getProperty("validatedToken");

        if (token != null) {
            TokenManager.invalidateToken(token);
            return Response.ok("{\"message\":\"Logout effettuato con successo\"}").build();
        }

        return Response.status(Response.Status.BAD_REQUEST)
                .entity("{\"error\":\"Impossibile completare il logout\"}").build();
    }
}
