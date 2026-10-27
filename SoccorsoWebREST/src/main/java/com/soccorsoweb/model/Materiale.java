package com.soccorsoweb.model;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Materiale")
public class Materiale implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 50)
    private String tipo;

    @Column(length = 100)
    private String descrizione;

    @Column(name = "quantita_disponibile", nullable = false)
    private Integer quantitaDisponibile;

    @ManyToMany(mappedBy = "materiali")
    private Set<Missione> missioni = new HashSet<>();

    public Materiale() {
    }

    public Materiale(String tipo, String descrizione, Integer quantitaDisponibile) {
        this.tipo = tipo;
        this.descrizione = descrizione;
        this.quantitaDisponibile = quantitaDisponibile;
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

    public Integer getQuantitaDisponibile() {
        return quantitaDisponibile;
    }

    public void setQuantitaDisponibile(Integer quantitaDisponibile) {
        this.quantitaDisponibile = quantitaDisponibile;
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
        if (!(o instanceof Materiale))
            return false;
        Materiale other = (Materiale) o;
        return id != null && other.id != null && id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : System.identityHashCode(this);
    }

    @Override
    public String toString() {
        return "Materiale{"
                + "id=" + id + ", "
                + "tipo='" + tipo + "'" + ", "
                + "descrizione='" + descrizione + "'" + ", "
                + "quantitaDisponibile=" + quantitaDisponibile +
                '}';
    }
}
