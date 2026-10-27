package com.soccorsoweb.model;

import com.soccorsoweb.model.enums.Priorita;
import com.soccorsoweb.model.enums.StatoRichiesta;
import com.soccorsoweb.model.enums.Tipologia;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "RichiestaSoccorso")
public class RichiestaSoccorso implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nome_richiedente")
    private String nomeRichiedente;

    @Column(name = "data_ora_richiesta", nullable = false)
    private LocalDateTime dataOraRichiesta;

    @Column(nullable = false, length = 500)
    private String indirizzo;

    @Column(columnDefinition = "TEXT")
    private String descrizione;

    @Column(name = "contatto_email", length = 255, nullable = false)
    private String contattoEmail;

    @Column(name = "codice_convalida", length = 100)
    private String codiceConvalida;

    @Column(length = 255)
    private String foto;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Tipologia tipologia;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatoRichiesta stato;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Priorita priorita; //la priorita sta al buon senso della gente

    @OneToOne(mappedBy = "richiesta", fetch = FetchType.LAZY)
    private Missione missione;

    public RichiestaSoccorso() {
    }

    public RichiestaSoccorso(String nomeRichiedente, LocalDateTime dataOraRichiesta, String indirizzo,
            String descrizione, String contattoEmail, String codiceConvalida, String foto, 
            Tipologia tipologia, StatoRichiesta stato, Priorita priorita) {
        this.nomeRichiedente = nomeRichiedente;
        this.dataOraRichiesta = dataOraRichiesta;
        this.indirizzo = indirizzo;
        this.descrizione = descrizione;
        this.contattoEmail = contattoEmail;
        this.codiceConvalida = codiceConvalida;
        this.foto = foto;
        this.tipologia = tipologia;
        this.stato = stato;
        this.priorita = priorita;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNomeRichiedente() {
        return nomeRichiedente;
    }

    public void setNomeRichiedente(String nomeRichiedente) {
        this.nomeRichiedente = nomeRichiedente;
    }

    public LocalDateTime getDataOraRichiesta() {
        return dataOraRichiesta;
    }

    public void setDataOraRichiesta(LocalDateTime dataOraRichiesta) {
        this.dataOraRichiesta = dataOraRichiesta;
    }

    public String getIndirizzo() {
        return indirizzo;
    }

    public void setIndirizzo(String indirizzo) {
        this.indirizzo = indirizzo;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public String getContattoEmail() {
        return contattoEmail;
    }

    public void setContattoEmail(String contattoEmail) {
        this.contattoEmail = contattoEmail;
    }

    public String getCodiceConvalida() {
        return codiceConvalida;
    }

    public void setCodiceConvalida(String codiceConvalida) {
        this.codiceConvalida = codiceConvalida;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public Tipologia getTipologia() {
        return tipologia;
    }

    public void setTipologia(Tipologia tipologia) {
        this.tipologia = tipologia;
    }

    public StatoRichiesta getStato() {
        return stato;
    }

    public void setStato(StatoRichiesta stato) {
        this.stato = stato;
    }

    public Priorita getPriorita() {
        return priorita;
    }

    public void setPriorita(Priorita priorita) {
        this.priorita = priorita;
    }
     
    public Missione getMissione() {
        return missione;
    }

    public void setMissione(Missione missione) {
        this.missione = missione;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof RichiestaSoccorso))
            return false;
        RichiestaSoccorso other = (RichiestaSoccorso) o;
        return id != null && other.id != null && id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : System.identityHashCode(this);
    }

    @Override
    public String toString() {
        return "RichiestaSoccorso{"
                + "id=" + id +
                ", nomeRichiedente='" + nomeRichiedente + "'"
                + ", dataOraRichiesta=" + dataOraRichiesta +
                ", indirizzo='" + indirizzo + "'"
                + ", descrizione='" + descrizione + "'"
                + ", contattoEmail='" + contattoEmail + "'"
                + ", codiceConvalida='" + codiceConvalida + "'"
                + ", foto='" + foto + "'"
                + ", tipologia=" + tipologia +
                ", stato=" + stato +
                ", priorita=" + priorita +
                '}';
    }
}
