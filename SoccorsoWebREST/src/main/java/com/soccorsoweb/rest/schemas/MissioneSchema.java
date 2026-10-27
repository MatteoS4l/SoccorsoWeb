package com.soccorsoweb.rest.schemas;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class MissioneSchema {

    private Integer id;
    private String obiettivo;
    private String stato;
    private LocalDateTime dataOraInizio;
    private LocalDateTime dataOraFine;
    private Integer livelloSuccesso;
    private String note;


    private Integer richiestaId;
    private Integer adminId;
    private Integer caposquadraId;
    private List<Integer> operatoriIds;

 
    private RichiestaSoccorsoSchema richiesta;
    private UtenteSchema caposquadra;
    private List<UtenteSchema> operatori;

    public MissioneSchema() {}

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getObiettivo() { return obiettivo; }
    public void setObiettivo(String obiettivo) { this.obiettivo = obiettivo; }

    public String getStato() { return stato; }
    public void setStato(String stato) { this.stato = stato; }

    public LocalDateTime getDataOraInizio() { return dataOraInizio; }
    public void setDataOraInizio(LocalDateTime dataOraInizio) { this.dataOraInizio = dataOraInizio; }

    public LocalDateTime getDataOraFine() { return dataOraFine; }
    public void setDataOraFine(LocalDateTime dataOraFine) { this.dataOraFine = dataOraFine; }

    public Integer getLivelloSuccesso() { return livelloSuccesso; }
    public void setLivelloSuccesso(Integer livelloSuccesso) { this.livelloSuccesso = livelloSuccesso; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }

    public Integer getRichiestaId() { return richiestaId; }
    public void setRichiestaId(Integer richiestaId) { this.richiestaId = richiestaId; }

    public Integer getAdminId() { return adminId; }
    public void setAdminId(Integer adminId) { this.adminId = adminId; }

    public Integer getCaposquadraId() { return caposquadraId; }
    public void setCaposquadraId(Integer caposquadraId) { this.caposquadraId = caposquadraId; }

    public List<Integer> getOperatoriIds() { return operatoriIds; }
    public void setOperatoriIds(List<Integer> operatoriIds) { this.operatoriIds = operatoriIds; }

    public RichiestaSoccorsoSchema getRichiesta() { return richiesta; }
    public void setRichiesta(RichiestaSoccorsoSchema richiesta) { this.richiesta = richiesta; }

    public UtenteSchema getCaposquadra() { return caposquadra; }
    public void setCaposquadra(UtenteSchema caposquadra) { this.caposquadra = caposquadra; }

    public List<UtenteSchema> getOperatori() { return operatori; }
    public void setOperatori(List<UtenteSchema> operatori) { this.operatori = operatori; }
}
