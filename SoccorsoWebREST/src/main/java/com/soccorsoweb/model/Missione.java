package com.soccorsoweb.model;

import com.soccorsoweb.model.enums.StatoMissione;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Missione")
public class Missione implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "TEXT")
    private String obiettivo;

    @Column(name = "data_ora_inizio", nullable = false)
    private LocalDateTime dataOraInizio;

    @Column(name = "data_ora_fine")
    private LocalDateTime dataOraFine;

    @Column(name = "livello_successo")
    private Integer livelloSuccesso;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatoMissione stato;

    @Column(columnDefinition = "TEXT")
    private String note;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "richiesta_id", nullable = false, unique = true)
    private RichiestaSoccorso richiesta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id", nullable = false)
    private Utente admin;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "caposquadra_id")
    private Utente caposquadra;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "Missione_Operatore", joinColumns = @JoinColumn(name = "missione_id"), inverseJoinColumns = @JoinColumn(name = "operatore_id"))
    private Set<Utente> operatori = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "Missione_Mezzo", joinColumns = @JoinColumn(name = "missione_id"), inverseJoinColumns = @JoinColumn(name = "mezzo_id"))
    private Set<Mezzo> mezzi = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "Missione_Materiale", joinColumns = @JoinColumn(name = "missione_id"), inverseJoinColumns = @JoinColumn(name = "materiale_id"))
    private Set<Materiale> materiali = new HashSet<>();

    @OneToMany(mappedBy = "missione", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<LogMissione> logMissioni = new HashSet<>();

    public Missione() {
    }

    public Missione(RichiestaSoccorso richiesta, Utente admin, Utente caposquadra, String obiettivo,
            LocalDateTime dataOraInizio, LocalDateTime dataOraFine, StatoMissione stato, String note) {
        this.richiesta = richiesta;
        this.admin = admin;
        this.caposquadra = caposquadra;
        this.obiettivo = obiettivo;
        this.dataOraInizio = dataOraInizio;
        this.dataOraFine = dataOraFine;
        this.stato = stato;
        this.note = note;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public RichiestaSoccorso getRichiesta() {
        return richiesta;
    }

    public void setRichiesta(RichiestaSoccorso richiesta) {
        this.richiesta = richiesta;
    }

    public Utente getAdmin() {
        return admin;
    }

    public void setAdmin(Utente admin) {
        this.admin = admin;
    }

    public Utente getCaposquadra() {
        return caposquadra;
    }

    public void setCaposquadra(Utente caposquadra) {
        this.caposquadra = caposquadra;
    }

    public String getObiettivo() {
        return obiettivo;
    }

    public void setObiettivo(String obiettivo) {
        this.obiettivo = obiettivo;
    }

    public LocalDateTime getDataOraInizio() {
        return dataOraInizio;
    }

    public void setDataOraInizio(LocalDateTime dataOraInizio) {
        this.dataOraInizio = dataOraInizio;
    }

    public LocalDateTime getDataOraFine() {
        return dataOraFine;
    }

    public void setDataOraFine(LocalDateTime dataOraFine) {
        this.dataOraFine = dataOraFine;
    }

    public Integer getLivelloSuccesso() {
        return livelloSuccesso;
    }

    public void setLivelloSuccesso(Integer livelloSuccesso) {
        this.livelloSuccesso = livelloSuccesso;
    }

    public StatoMissione getStato() {
        return stato;
    }

    public void setStato(StatoMissione stato) {
        this.stato = stato;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Set<Utente> getOperatori() {
        return operatori;
    }

    public void setOperatori(Set<Utente> operatori) {
        this.operatori = operatori;
    }

    public Set<Mezzo> getMezzi() {
        return mezzi;
    }

    public void setMezzi(Set<Mezzo> mezzi) {
        this.mezzi = mezzi;
    }

    public Set<Materiale> getMateriali() {
        return materiali;
    }

    public void setMateriali(Set<Materiale> materiali) {
        this.materiali = materiali;
    }

    public Set<LogMissione> getLogMissioni() {
        return logMissioni;
    }

    public void setLogMissioni(Set<LogMissione> logMissioni) {
        this.logMissioni = logMissioni;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Missione))
            return false;
        Missione other = (Missione) o;
        return id != null && other.id != null && id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : System.identityHashCode(this);
    }

    @Override
    public String toString() {
        return "Missione{"
                + "id=" + id +
                ", obiettivo='" + obiettivo + '\'' +
                ", dataOraInizio=" + dataOraInizio +
                ", dataOraFine=" + dataOraFine +
                ", stato=" + stato +
                ", note='" + note + '\'' +
                '}';
    }
}
