package com.soccorsoweb.rest.resources;

import com.soccorsoweb.data.DAOFactory;
import com.soccorsoweb.data.RichiestaSoccorsoDAO;
import com.soccorsoweb.model.RichiestaSoccorso;
import com.soccorsoweb.model.Utente;
import com.soccorsoweb.model.enums.StatoRichiesta;
import com.soccorsoweb.model.enums.Tipologia;
import com.soccorsoweb.model.enums.Priorita;
import com.soccorsoweb.rest.schemas.RichiestaSoccorsoSchema;
import com.soccorsoweb.rest.security.Logged;
import com.soccorsoweb.model.enums.Ruolo;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.*;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Path("/richieste")
public class RichiesteSoccorsoResource {

    @GET
    @Path("/tipologie")
    @Produces(MediaType.APPLICATION_JSON)
    public List<String> getTipologie() {
        return java.util.Arrays.stream(Tipologia.values()).map(Enum::name).collect(Collectors.toList());
    }

    @GET
    @Path("/stati")
    @Produces(MediaType.APPLICATION_JSON)
    public List<String> getStati() {
        return java.util.Arrays.stream(StatoRichiesta.values()).map(Enum::name).collect(Collectors.toList());
    }

    @GET
    @Path("/priorita")
    @Produces(MediaType.APPLICATION_JSON)
    public List<String> getPriorita() {
        return java.util.Arrays.stream(Priorita.values()).map(Enum::name).collect(Collectors.toList());
    }

    @Logged
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRichieste(
            @Context ContainerRequestContext crc,
            @Context HttpServletRequest request,
            @QueryParam("stato") String stato,
            @QueryParam("page") @DefaultValue("1") int page,
            @QueryParam("pageSize") @DefaultValue("10") int pageSize) {

        // --- CONTROLLO RUOLO ---
        Utente currentUser = getCurrentUser(crc);
        if (currentUser == null || currentUser.getRuolo() != Ruolo.ADMIN) {
            return Response.status(Response.Status.FORBIDDEN)
                    .entity("{\"error\":\"Accesso riservato agli amministratori\"}").build();
        }

        RichiestaSoccorsoDAO dao = DAOFactory.getRichiestaSoccorsoDAO(request);
        List<RichiestaSoccorso> richieste;

        if (stato != null && !stato.isEmpty()) {
            try {
                StatoRichiesta s = StatoRichiesta.valueOf(stato.toUpperCase());
                richieste = dao.findByStatoPaginated(s, page, pageSize);
            } catch (IllegalArgumentException e) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("{\"error\":\"Stato non valido\"}").build();
            }
        } else {
            richieste = dao.findAllPaginated(page, pageSize);
        }

        List<RichiestaSoccorsoSchema> listaSchemi = richieste.stream()
                .map(this::mapToSchema)
                .collect(Collectors.toList());

        return Response.ok(listaSchemi).build();
    }

    @Logged
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRichiesta(@Context ContainerRequestContext crc, @Context HttpServletRequest request, @PathParam("id") Integer id) {
        
        // --- CONTROLLO RUOLO ---
        Utente currentUser = getCurrentUser(crc);
        if (currentUser == null || currentUser.getRuolo() != Ruolo.ADMIN) {
            return Response.status(Response.Status.FORBIDDEN)
                    .entity("{\"error\":\"Accesso riservato agli amministratori\"}").build();
        }

        RichiestaSoccorsoDAO dao = DAOFactory.getRichiestaSoccorsoDAO(request);
        RichiestaSoccorso richiesta = dao.findById(id);

        if (richiesta == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.ok(mapToSchema(richiesta)).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createRichiesta(@Context HttpServletRequest request, RichiestaSoccorsoSchema schema) {
        if (schema == null || schema.getContattoEmail() == null || schema.getIndirizzo() == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\":\"Dati obbligatori mancanti (email, indirizzo)\"}").build();
        }

        RichiestaSoccorsoDAO dao = DAOFactory.getRichiestaSoccorsoDAO(request);

        RichiestaSoccorso modello = new RichiestaSoccorso();
        modello.setNomeRichiedente(schema.getNomeRichiedente());
        modello.setContattoEmail(schema.getContattoEmail());
        modello.setIndirizzo(schema.getIndirizzo());
        modello.setDescrizione(schema.getDescrizione());
        modello.setDataOraRichiesta(LocalDateTime.now());

        try {
            modello.setTipologia(Tipologia.valueOf(schema.getTipologia().toUpperCase()));
        } catch (Exception e) {
            modello.setTipologia(Tipologia.ALTRO);
        }

        try {
            if (schema.getPriorita() != null) {
                modello.setPriorita(Priorita.valueOf(schema.getPriorita().toUpperCase()));
            } else {
                modello.setPriorita(Priorita.MEDIA);
            }
        } catch (Exception e) {
            modello.setPriorita(Priorita.MEDIA);
        }

        modello.setStato(StatoRichiesta.DA_CONVALIDARE);
        modello.setCodiceConvalida(java.util.UUID.randomUUID().toString());

        dao.save(modello);

        return Response.status(Response.Status.CREATED).entity(mapToSchema(modello)).build();
    }

    @GET
    @Path("/convalida/{token}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response convalidaRichiesta(@Context HttpServletRequest request, @PathParam("token") String token) {
        RichiestaSoccorsoDAO dao = DAOFactory.getRichiestaSoccorsoDAO(request);

        RichiestaSoccorso richiesta = dao.findByCodiceConvalida(token);

        if (richiesta != null && richiesta.getStato() == StatoRichiesta.DA_CONVALIDARE) {
            richiesta.setStato(StatoRichiesta.IN_ATTESA);
            dao.save(richiesta);
            return Response.ok("{\"message\":\"Richiesta convalidata con successo\"}").build();
        }

        return Response.status(Response.Status.NOT_FOUND)
                .entity("{\"error\":\"Token non valido o richiesta già convalidata\"}").build();
    }

        @Logged
        @GET
        @Path("/insuccessi")
        @Produces(MediaType.APPLICATION_JSON)
        public Response getInsuccessi(@Context ContainerRequestContext crc, @Context HttpServletRequest request) {
            
            // --- CONTROLLO RUOLO ---
            Utente currentUser = getCurrentUser(crc);
            if (currentUser == null || currentUser.getRuolo() != Ruolo.ADMIN) {
                return Response.status(Response.Status.FORBIDDEN)
                        .entity("{\"error\":\"Accesso riservato agli amministratori\"}").build();
            }
    
            RichiestaSoccorsoDAO dao = DAOFactory.getRichiestaSoccorsoDAO(request);
            
            List<RichiestaSoccorsoSchema> insuccessi = dao.findAll().stream()                .filter(r -> r.getMissione() != null
                        && r.getMissione().getLivelloSuccesso() != null
                        && r.getMissione().getLivelloSuccesso() < 5)
                .map(this::mapToSchema)
                .collect(Collectors.toList());

        return Response.ok(insuccessi).build();
    }

    @Logged
    @POST
    @Path("/{id}/rifiuta")
    @Produces(MediaType.APPLICATION_JSON)
    public Response rifiutaRichiesta(@Context ContainerRequestContext crc, @Context HttpServletRequest request,
            @PathParam("id") Integer id) {
        // --- CONTROLLO SICUREZZA ---
        Utente currentUser = getCurrentUser(crc);
        if (currentUser == null || currentUser.getRuolo() != Ruolo.ADMIN) {
            return Response.status(Response.Status.FORBIDDEN)
                    .entity("{\"error\":\"Solo l'Amministratore può rifiutare una richiesta\"}")
                    .build();
        }
        
        RichiestaSoccorsoDAO dao = DAOFactory.getRichiestaSoccorsoDAO(request);
        RichiestaSoccorso richiesta = dao.findById(id);

        if (richiesta == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        if (richiesta.getStato() == StatoRichiesta.IN_ATTESA || richiesta.getStato() == StatoRichiesta.DA_CONVALIDARE) {
            richiesta.setStato(StatoRichiesta.RIFIUTATA);
            dao.save(richiesta);
            return Response.ok("{\"message\":\"Richiesta rifiutata con successo\"}").build();
        }

        return Response.status(Response.Status.BAD_REQUEST)
                .entity("{\"error\":\"Impossibile rifiutare una richiesta già in corso o chiusa\"}").build();
    }

    private Utente getCurrentUser(ContainerRequestContext crc) {
        return (Utente) crc.getProperty("currentUser");
    }

    private RichiestaSoccorsoSchema mapToSchema(RichiestaSoccorso modello) {
        RichiestaSoccorsoSchema schema = new RichiestaSoccorsoSchema();
        schema.setId(modello.getId());
        schema.setNomeRichiedente(modello.getNomeRichiedente());
        schema.setContattoEmail(modello.getContattoEmail());
        schema.setIndirizzo(modello.getIndirizzo());
        schema.setDescrizione(modello.getDescrizione());
        schema.setTipologia(modello.getTipologia().name());
        schema.setStato(modello.getStato().name());
        schema.setPriorita(modello.getPriorita().name());
        schema.setDataOraRichiesta(modello.getDataOraRichiesta());
        schema.setCodiceConvalida(modello.getCodiceConvalida());
        schema.setFoto(modello.getFoto());
        return schema;
    }
}
