package com.soccorsoweb.model;

import com.soccorsoweb.model.enums.Ruolo;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Utente")
public class Utente implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    private String nome;

    private String cognome;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Ruolo ruolo;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "Utente_Patente", joinColumns = @JoinColumn(name = "utente_id"), inverseJoinColumns = @JoinColumn(name = "patente_id"))
    private Set<Patente> patenti = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "Utente_Skill", joinColumns = @JoinColumn(name = "utente_id"), inverseJoinColumns = @JoinColumn(name = "skill_id"))
    private Set<Skill> skills = new HashSet<>();

    @OneToMany(mappedBy = "admin", fetch = FetchType.LAZY)
    private Set<Missione> missioniAmministrate = new HashSet<>();

    @OneToMany(mappedBy = "caposquadra", fetch = FetchType.LAZY)
    private Set<Missione> missioniCaposquadra = new HashSet<>();

    @ManyToMany(mappedBy = "operatori", fetch = FetchType.LAZY)
    private Set<Missione> missioniAssegnate = new HashSet<>();

    @OneToMany(mappedBy = "operatore", fetch = FetchType.LAZY)
    private Set<LogMissione> logMissioni = new HashSet<>();

    public Utente() {

    }

    public Utente(String email, String password, String nome, String cognome, Ruolo ruolo) {

        this.email = email;

        this.password = password;

        this.nome = nome;

        this.cognome = cognome;

        this.ruolo = ruolo;

    }

    public Integer getId() {

        return id;

    }

    public void setId(Integer id) {

        this.id = id;

    }

    public String getEmail() {

        return email;

    }

    public void setEmail(String email) {

        this.email = email;

    }

    public String getPassword() {

        return password;

    }

    public void setPassword(String password) {

        this.password = password;

    }

    public String getNome() {

        return nome;

    }

    public void setNome(String nome) {

        this.nome = nome;

    }

    public String getCognome() {

        return cognome;

    }

    public void setCognome(String cognome) {

        this.cognome = cognome;

    }

    public Ruolo getRuolo() {

        return ruolo;

    }

    public void setRuolo(Ruolo ruolo) {

        this.ruolo = ruolo;

    }

    public Set<Patente> getPatenti() {

        return patenti;

    }

    public void setPatenti(Set<Patente> patenti) {

        this.patenti = patenti;

    }

    public Set<Skill> getSkills() {

        return skills;

    }

    public void setSkills(Set<Skill> skills) {

        this.skills = skills;

    }

    public Set<Missione> getMissioniAssegnate() {

        return missioniAssegnate;

    }

    public void setMissioniAssegnate(Set<Missione> missioniAssegnate) {

        this.missioniAssegnate = missioniAssegnate;

    }

    @Override

    public boolean equals(Object o) {

        if (this == o)

            return true;

        if (!(o instanceof Utente))
            return false;
        Utente utente = (Utente) o;
        return id != null && utente.id != null && id.equals(utente.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : System.identityHashCode(this);
    }

    @Override
    public String toString() {
        return "Utente{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", nome='" + nome + '\'' +
                ", cognome='" + cognome + '\'' +
                ", ruolo=" + ruolo +
                '}';
    }
}
