package com.soccorsoweb.model;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Patente")
public class Patente implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "tipologia", nullable = false, unique = true, length = 50)
    private String tipologia;

    @ManyToMany(mappedBy = "patenti")
    private Set<Utente> utenti = new HashSet<>();


    public Patente() {
    }

    public Patente(String tipologia) {
        this.tipologia = tipologia;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTipologia() {
        return tipologia;
    }

    public void setTipologia(String tipologia) {
        this.tipologia = tipologia;
    }

    public Set<Utente> getUtenti() {
        return utenti;
    }

    public void setUtenti(Set<Utente> utenti) {
        this.utenti = utenti;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Patente))
            return false;
        Patente other = (Patente) o;
        return id != null && other.id != null && id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : System.identityHashCode(this);
    }

    @Override
    public String toString() {
        return "Patente{"
                + "id=" + id + ", "
                + "tipologia='" + tipologia + "'"
                + '}';
    }
}
