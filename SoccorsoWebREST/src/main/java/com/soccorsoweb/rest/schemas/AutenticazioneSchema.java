package com.soccorsoweb.rest.schemas;

import com.fasterxml.jackson.annotation.JsonInclude;

// Schema per login/logout

@JsonInclude(JsonInclude.Include.NON_NULL) 
public class AutenticazioneSchema {

    
    private String email;
    private String password;

  
    private String token;
    private UtenteSchema utente;

    public AutenticazioneSchema() {}

   
    public AutenticazioneSchema(String token, UtenteSchema utente) {
        this.token = token;
        this.utente = utente;
    }

    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public UtenteSchema getUtente() { return utente; }
    public void setUtente(UtenteSchema utente) { this.utente = utente; }
}
