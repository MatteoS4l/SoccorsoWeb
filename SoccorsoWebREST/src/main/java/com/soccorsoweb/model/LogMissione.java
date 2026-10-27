package com.soccorsoweb.model;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "LogMissione")
public class LogMissione implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "data_ora", nullable = false)
    private LocalDateTime dataOra;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String descrizione;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "missione_id", nullable = false)
    private Missione missione;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "operatore_id")
    private Utente operatore;

    public LogMissione() {
    }

    public LogMissione(Missione missione, Utente operatore, LocalDateTime dataOra, String descrizione) {
        this.missione = missione;
        this.operatore = operatore;
        this.dataOra = dataOra;
        this.descrizione = descrizione;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Missione getMissione() {
        return missione;
    }

    public void setMissione(Missione missione) {
        this.missione = missione;
    }

    public Utente getOperatore() {
        return operatore;
    }

    public void setOperatore(Utente operatore) {
        this.operatore = operatore;
    }

    public LocalDateTime getDataOra() {
        return dataOra;
    }

    public void setDataOra(LocalDateTime dataOra) {
        this.dataOra = dataOra;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof LogMissione))
            return false;
        LogMissione other = (LogMissione) o;
        return id != null && other.id != null && id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : System.identityHashCode(this);
    }

    @Override
    public String toString() {
        return "LogMissione{"
                + "id=" + id + ", "
                + "dataOra=" + dataOra + ", "
                + "descrizione='" + descrizione + "'"
                + "}";
    }
}
