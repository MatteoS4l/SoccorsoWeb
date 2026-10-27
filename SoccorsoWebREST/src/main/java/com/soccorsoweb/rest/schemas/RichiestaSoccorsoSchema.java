package com.soccorsoweb.rest.schemas;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class RichiestaSoccorsoSchema {

    private Integer id;
    private String nomeRichiedente;
    private String contattoEmail;
    private String indirizzo;
    private String descrizione;
    private String tipologia;
    private String priorita;
    private String stato;
    private LocalDateTime dataOraRichiesta;
    private String codiceConvalida;
    private String foto;

    public RichiestaSoccorsoSchema() {}

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getNomeRichiedente() { return nomeRichiedente; }
    public void setNomeRichiedente(String nomeRichiedente) { this.nomeRichiedente = nomeRichiedente; }

    public String getContattoEmail() { return contattoEmail; }
    public void setContattoEmail(String contattoEmail) { this.contattoEmail = contattoEmail; }

    public String getIndirizzo() { return indirizzo; }
    public void setIndirizzo(String indirizzo) { this.indirizzo = indirizzo; }

    public String getDescrizione() { return descrizione; }
    public void setDescrizione(String descrizione) { this.descrizione = descrizione; }

    public String getTipologia() { return tipologia; }
    public void setTipologia(String tipologia) { this.tipologia = tipologia; }

    public String getPriorita() { return priorita; }
    public void setPriorita(String priorita) { this.priorita = priorita; }

    public String getStato() { return stato; }
    public void setStato(String stato) { this.stato = stato; }

    public LocalDateTime getDataOraRichiesta() { return dataOraRichiesta; }
    public void setDataOraRichiesta(LocalDateTime dataOraRichiesta) { this.dataOraRichiesta = dataOraRichiesta; }

    public String getCodiceConvalida() { return codiceConvalida; }
    public void setCodiceConvalida(String codiceConvalida) { this.codiceConvalida = codiceConvalida; }

    public String getFoto() { return foto; }
    public void setFoto(String foto) { this.foto = foto; }
}
