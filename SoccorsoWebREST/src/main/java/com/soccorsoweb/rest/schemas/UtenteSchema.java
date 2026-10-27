package com.soccorsoweb.rest.schemas;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class UtenteSchema {
    private Integer id;
    private String nome;
    private String cognome;
    private String email;
    private String ruolo;
    private List<String> skills;
    private List<String> patenti;
    
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<String> missioniPartecipate;

    public UtenteSchema() {}

    public UtenteSchema(Integer id, String nome, String cognome, String email, String ruolo) {
        this.id = id;
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
        this.ruolo = ruolo;
    }


    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getCognome() { return cognome; }
    public void setCognome(String cognome) { this.cognome = cognome; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getRuolo() { return ruolo; }
    public void setRuolo(String ruolo) { this.ruolo = ruolo; }

    public List<String> getSkills() { return skills; }
    public void setSkills(List<String> skills) { this.skills = skills; }

    public List<String> getPatenti() { return patenti; }
    public void setPatenti(List<String> patenti) { this.patenti = patenti; }

    public List<String> getMissioniPartecipate() { return missioniPartecipate; }
    public void setMissioniPartecipate(List<String> missioniPartecipate) { this.missioniPartecipate = missioniPartecipate; }
}
