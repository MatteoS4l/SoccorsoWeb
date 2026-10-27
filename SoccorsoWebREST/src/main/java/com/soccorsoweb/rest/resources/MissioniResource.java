package com.soccorsoweb.rest.resources;

import com.soccorsoweb.data.DAOFactory;
import com.soccorsoweb.data.MissioneDAO;
import com.soccorsoweb.data.RichiestaSoccorsoDAO;
import com.soccorsoweb.data.UtenteDAO;
import com.soccorsoweb.model.Missione;
import com.soccorsoweb.model.RichiestaSoccorso;
import com.soccorsoweb.model.Utente;
import com.soccorsoweb.model.enums.StatoMissione;
import com.soccorsoweb.model.enums.StatoRichiesta;
import com.soccorsoweb.rest.schemas.MissioneSchema;
import com.soccorsoweb.rest.schemas.RichiestaSoccorsoSchema;
import com.soccorsoweb.rest.schemas.UtenteSchema;
import com.soccorsoweb.rest.security.Logged;
import com.soccorsoweb.model.enums.Ruolo;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.*;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Logged
@Path("/missioni")
public class MissioniResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMissioni(@Context HttpServletRequest request) {
        MissioneDAO dao = DAOFactory.getMissioneDAO(request);
        List<MissioneSchema> lista = dao.findAll().stream()
                .map(this::mapToSchema)
                .collect(Collectors.toList());
        return Response.ok(lista).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMissione(@Context HttpServletRequest request, @PathParam("id") Integer id) {
        MissioneDAO dao = DAOFactory.getMissioneDAO(request);
        Missione missione = dao.findById(id);

        if (missione == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.ok(mapToSchema(missione)).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createMissione(@Context ContainerRequestContext crc, @Context HttpServletRequest request, MissioneSchema schema) {
        
        // --- CONTROLLO RUOLO ---
        Utente currentUser = getCurrentUser(crc);
        if (currentUser == null || currentUser.getRuolo() != Ruolo.ADMIN) {
            return Response.status(Response.Status.FORBIDDEN)
                    .entity("{\"error\":\"Solo l'Amministratore può avviare nuove missioni\"}").build();
        }

        if (schema == null || schema.getRichiestaId() == null || schema.getAdminId() == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("{\"error\":\"Dati insufficienti\"}").build();
        }

        MissioneDAO missioneDAO = DAOFactory.getMissioneDAO(request);
        RichiestaSoccorsoDAO richiestaDAO = DAOFactory.getRichiestaSoccorsoDAO(request);
        UtenteDAO utenteDAO = DAOFactory.getUtenteDAO(request);

        RichiestaSoccorso richiesta = richiestaDAO.findById(schema.getRichiestaId());
        Utente admin = utenteDAO.findById(schema.getAdminId());

        if (richiesta == null || admin == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("{\"error\":\"Richiesta o Admin non trovati\"}")
                    .build();
        }

        Missione missione = new Missione();
        missione.setRichiesta(richiesta);
        missione.setAdmin(admin);
        missione.setObiettivo(schema.getObiettivo());
        missione.setDataOraInizio(LocalDateTime.now());
        missione.setStato(StatoMissione.ATTIVA);

        if (schema.getCaposquadraId() != null) {
            missione.setCaposquadra(utenteDAO.findById(schema.getCaposquadraId()));
        }

        if (schema.getOperatoriIds() != null) {
            Set<Utente> operatori = new HashSet<>();
            for (Integer opId : schema.getOperatoriIds()) {
                Utente op = utenteDAO.findById(opId);
                if (op != null)
                    operatori.add(op);
            }
            missione.setOperatori(operatori);
        }

        richiesta.setStato(StatoRichiesta.CONFERMATA);
        richiestaDAO.save(richiesta);

        missioneDAO.save(missione);

        return Response.status(Response.Status.CREATED).entity(mapToSchema(missione)).build();
    }

    @POST
    @Path("/{id}/chiusura")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response chiudiMissione(@Context ContainerRequestContext crc, @Context HttpServletRequest request,
            @PathParam("id") Integer id, MissioneSchema schema) {
        MissioneDAO missioneDAO = DAOFactory.getMissioneDAO(request);
        Missione missione = missioneDAO.findById(id);

        if (missione == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        Utente currentUser = getCurrentUser(crc);
        if (currentUser == null) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }

        boolean isAdmin = currentUser.getRuolo() == Ruolo.ADMIN;
        boolean isCaposquadra = missione.getCaposquadra() != null
                && missione.getCaposquadra().getId().equals(currentUser.getId());

        if (!isAdmin && !isCaposquadra) {
            return Response.status(Response.Status.FORBIDDEN)
                    .entity("{\"error\":\"Solo l'Admin o il Caposquadra della missione possono chiuderla\"}")
                    .build();
        }

        if (schema.getLivelloSuccesso() == null || schema.getLivelloSuccesso() < 1 || schema.getLivelloSuccesso() > 5) {
            return Response.status(Response.Status.BAD_REQUEST).entity("{\"error\":\"Valutazione (1-5) obbligatoria\"}")
                    .build();
        }

        missione.setStato(StatoMissione.COMPLETATA);
        missione.setDataOraFine(LocalDateTime.now());
        missione.setLivelloSuccesso(schema.getLivelloSuccesso());
        missione.setNote(schema.getNote());

        RichiestaSoccorso richiesta = missione.getRichiesta();
        richiesta.setStato(StatoRichiesta.CHIUSA);
        DAOFactory.getRichiestaSoccorsoDAO(request).save(richiesta);

        missioneDAO.save(missione);

        return Response.ok("{\"message\":\"Missione chiusa con successo\"}").build();
    }

    private Utente getCurrentUser(ContainerRequestContext crc) {
        return (Utente) crc.getProperty("currentUser");
    }

    private MissioneSchema mapToSchema(Missione m) {
        MissioneSchema schema = new MissioneSchema();
        schema.setId(m.getId());
        schema.setObiettivo(m.getObiettivo());
        schema.setStato(m.getStato().name());
        schema.setDataOraInizio(m.getDataOraInizio());
        schema.setDataOraFine(m.getDataOraFine());
        schema.setLivelloSuccesso(m.getLivelloSuccesso());
        schema.setNote(m.getNote());

        RichiestaSoccorsoSchema rSchema = new RichiestaSoccorsoSchema();
        rSchema.setId(m.getRichiesta().getId());
        rSchema.setNomeRichiedente(m.getRichiesta().getNomeRichiedente());
        rSchema.setContattoEmail(m.getRichiesta().getContattoEmail());
        rSchema.setIndirizzo(m.getRichiesta().getIndirizzo());
        rSchema.setDescrizione(m.getRichiesta().getDescrizione());
        rSchema.setTipologia(m.getRichiesta().getTipologia().name());
        rSchema.setPriorita(m.getRichiesta().getPriorita().name());
        rSchema.setStato(m.getRichiesta().getStato().name());
        rSchema.setDataOraRichiesta(m.getRichiesta().getDataOraRichiesta());
        rSchema.setCodiceConvalida(m.getRichiesta().getCodiceConvalida());
        rSchema.setFoto(m.getRichiesta().getFoto());
        schema.setRichiesta(rSchema);

        if (m.getCaposquadra() != null) {
            schema.setCaposquadra(new UtenteSchema(m.getCaposquadra().getId(), m.getCaposquadra().getNome(),
                    m.getCaposquadra().getCognome(), m.getCaposquadra().getEmail(),
                    m.getCaposquadra().getRuolo().name()));
        }

        schema.setOperatori(m.getOperatori().stream()
                .map(o -> new UtenteSchema(o.getId(), o.getNome(), o.getCognome(), o.getEmail(), o.getRuolo().name()))
                .collect(Collectors.toList()));

        return schema;
    }
}
