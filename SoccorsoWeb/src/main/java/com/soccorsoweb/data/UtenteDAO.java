package com.soccorsoweb.data;

import java.util.*;

import com.soccorsoweb.model.Utente;
import com.soccorsoweb.model.enums.Ruolo;
import com.soccorsoweb.model.enums.StatoMissione;
import com.soccorsoweb.security.Hashing;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;

public class UtenteDAO extends BaseDAO<Utente, Integer> {

    public UtenteDAO(EntityManager entityManager) {
        super(entityManager);
    }

    public Utente findByEmail(String email) {
        try {
            TypedQuery<Utente> query = entityManager.createQuery(
                    "SELECT u FROM Utente u WHERE u.email = :email", Utente.class);
            query.setParameter("email", email);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<Utente> findByRuolo(Ruolo ruolo) {
        TypedQuery<Utente> query = entityManager.createQuery(
                "SELECT u FROM Utente u WHERE u.ruolo = :ruolo", Utente.class);
        query.setParameter("ruolo", ruolo);
        return query.getResultList(); 
    }

    /**
     * Recupera tutti gli utenti con ruolo OPERATORE che non sono attualmente
     * assegnati come operatore o caposquadra a missioni con stato ATTIVA.
     */
    public List<Utente> findOperatoriLiberi() {
        TypedQuery<Utente> query = entityManager.createQuery(
                "SELECT u FROM Utente u WHERE u.ruolo = :ruolo " +
                "AND NOT EXISTS (SELECT m1 FROM Missione m1 WHERE m1.caposquadra = u AND m1.stato = :stato) " +
                "AND NOT EXISTS (SELECT m2 FROM Missione m2 JOIN m2.operatori o WHERE o = u AND m2.stato = :stato)", 
                Utente.class);
        query.setParameter("ruolo", Ruolo.OPERATORE);
        query.setParameter("stato", StatoMissione.ATTIVA);
        return query.getResultList();
    }

    public Utente checkLogin(String email, String password) {
            try {
                Utente utente = findByEmail(email);
                if (utente != null) {
                                    System.out.println("DEBUG: Password chiara (ricevuta): [" + password + "]");
                                    System.out.println("DEBUG: Password hashata (dal DB): [" + utente.getPassword() + "]");
                                    boolean passwordMatch = Hashing.checkPassword(password, utente.getPassword());
                                    System.out.println("DEBUG: Risultato Hashing.checkPassword: [" + passwordMatch + "]");
                                    if (passwordMatch) {                        return utente;
                    }
                }
                return null; 
            } catch (Exception e) {
                System.err.println("Errore durante il controllo del login per l'email: " + email + " - " + e.getMessage());
                return null; 
            }
        }}
