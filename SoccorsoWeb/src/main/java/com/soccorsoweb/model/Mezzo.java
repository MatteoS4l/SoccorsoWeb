package com.soccorsoweb.model;

import com.soccorsoweb.model.enums.StatoMezzo;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Mezzo")
public class Mezzo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 50)
    private String tipo;

    @Column(length = 100)
    private String descrizione;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatoMezzo disponibilita;

    @ManyToMany(mappedBy = "mezzi")
    private Set<Missione> missioni = new HashSet<>();

    public Mezzo() {
    }

    public Mezzo(String tipo, String descrizione, StatoMezzo disponibilita) {
        this.tipo = tipo;
        this.descrizione = descrizione;
        this.disponibilita = disponibilita;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public StatoMezzo getDisponibilita() {
        return disponibilita;
    }

    public void setDisponibilita(StatoMezzo disponibilita) {
        this.disponibilita = disponibilita;
    }

    public Set<Missione> getMissioni() {
        return missioni;
    }

    public void setMissioni(Set<Missione> missioni) {
        this.missioni = missioni;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Mezzo))
            return false;
        Mezzo other = (Mezzo) o;
        return id != null && other.id != null && id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : System.identityHashCode(this);
    }

    @Override
    public String toString() {
        return "Mezzo{"
                + "id=" + id + ", "
                + "tipo='" + tipo + "'" + ", "
                + "descrizione='" + descrizione + "'" + ", "
                + "disponibilita=" + disponibilita +
                '}';
    }
}
