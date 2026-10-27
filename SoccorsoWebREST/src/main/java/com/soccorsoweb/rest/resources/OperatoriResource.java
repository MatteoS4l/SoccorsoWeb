package com.soccorsoweb.rest.resources;

import com.soccorsoweb.data.DAOFactory;
import com.soccorsoweb.data.UtenteDAO;
import com.soccorsoweb.model.Utente;
import com.soccorsoweb.model.enums.Ruolo;
import com.soccorsoweb.rest.schemas.UtenteSchema;
import com.soccorsoweb.rest.schemas.MissioneSchema;
import com.soccorsoweb.rest.schemas.RichiestaSoccorsoSchema;
import com.soccorsoweb.rest.security.Logged;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.*;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

@Logged
@Path("/operatori")
public class OperatoriResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOperatori(@Context ContainerRequestContext crc, @Context HttpServletRequest request, @QueryParam("stato") String stato) {
    
        Utente currentUser = getCurrentUser(crc);
        if (currentUser == null || currentUser.getRuolo() != Ruolo.ADMIN) {
            return Response.status(Response.Status.FORBIDDEN)
                    .entity("{\"error\":\"Accesso riservato agli amministratori\"}").build();
        }

        UtenteDAO dao = DAOFactory.getUtenteDAO(request);
        List<Utente> operatori;

        if ("libero".equalsIgnoreCase(stato)) {
            operatori = dao.findOperatoriLiberi();
        } else {
            operatori = dao.findByRuolo(Ruolo.OPERATORE);
        }

        List<UtenteSchema> schemi = operatori.stream()
                .map(this::mapToSchema)
                .collect(Collectors.toList());

        return Response.ok(schemi).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOperatore(@Context ContainerRequestContext crc, @Context HttpServletRequest request, @PathParam("id") Integer id) {
        
    
        Utente currentUser = getCurrentUser(crc);
        if (currentUser == null || currentUser.getRuolo() != Ruolo.ADMIN) {
            return Response.status(Response.Status.FORBIDDEN)
                    .entity("{\"error\":\"Accesso riservato agli amministratori\"}").build();
        }

        UtenteDAO dao = DAOFactory.getUtenteDAO(request);
        Utente utente = dao.findByIdFull(id);

        if (utente == null || utente.getRuolo() != Ruolo.OPERATORE) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.ok(mapToSchema(utente)).build();
    }

    @GET
    @Path("/{id}/missioni")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMissioniOperatore(@Context ContainerRequestContext crc, @Context HttpServletRequest request, @PathParam("id") Integer id) {
    
        Utente currentUser = getCurrentUser(crc);
        if (currentUser == null) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        // operatore vede le sue, admin tuttew
        if (currentUser.getRuolo() != Ruolo.ADMIN && !currentUser.getId().equals(id)) {
            return Response.status(Response.Status.FORBIDDEN)
                    .entity("{\"error\":\"Non hai i permessi per visualizzare le missioni di un altro operatore\"}").build();
        }

        UtenteDAO dao = DAOFactory.getUtenteDAO(request);
        Utente utente = dao.findByIdWithMissioni(id);

        if (utente == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        List<MissioneSchema> missioni = utente.getMissioniAssegnate().stream()
                .map(m -> {
                    MissioneSchema ms = new MissioneSchema();
                    ms.setId(m.getId());
                    ms.setObiettivo(m.getObiettivo());
                    ms.setStato(m.getStato().name());
                    ms.setDataOraInizio(m.getDataOraInizio());
                    ms.setDataOraFine(m.getDataOraFine());
                    ms.setLivelloSuccesso(m.getLivelloSuccesso());
                    ms.setNote(m.getNote());
                    
                    RichiestaSoccorsoSchema rs = new RichiestaSoccorsoSchema();
                    rs.setId(m.getRichiesta().getId());
                    rs.setNomeRichiedente(m.getRichiesta().getNomeRichiedente());
                    rs.setContattoEmail(m.getRichiesta().getContattoEmail());
                    rs.setIndirizzo(m.getRichiesta().getIndirizzo());
                    rs.setDescrizione(m.getRichiesta().getDescrizione());
                    rs.setTipologia(m.getRichiesta().getTipologia().name());
                    rs.setStato(m.getRichiesta().getStato().name());
                    rs.setPriorita(m.getRichiesta().getPriorita().name());
                    rs.setDataOraRichiesta(m.getRichiesta().getDataOraRichiesta());
                    
                    ms.setRichiesta(rs);
                    
                    return ms;
                })
                .collect(Collectors.toList());

        return Response.ok(missioni).build();
    }

    private Utente getCurrentUser(ContainerRequestContext crc) {
        return (Utente) crc.getProperty("currentUser");
    }

    private UtenteSchema mapToSchema(Utente u) {        UtenteSchema schema = new UtenteSchema();
        schema.setId(u.getId());
        schema.setNome(u.getNome());
        schema.setCognome(u.getCognome());
        schema.setEmail(u.getEmail());
        schema.setRuolo(u.getRuolo().name());

        if (u.getSkills() != null) {
            schema.setSkills(u.getSkills().stream().map(s -> s.getDescrizione()).collect(Collectors.toList()));
        }

        if (u.getPatenti() != null) {
            schema.setPatenti(u.getPatenti().stream().map(p -> p.getTipologia()).collect(Collectors.toList()));
        }

        return schema;
    }
}
