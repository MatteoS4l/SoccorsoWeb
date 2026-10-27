package com.soccorsoweb.data;

import com.soccorsoweb.model.RichiestaSoccorso;
import com.soccorsoweb.model.enums.StatoRichiesta;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;

public class RichiestaSoccorsoDAO extends BaseDAO<RichiestaSoccorso, Integer> {

    public RichiestaSoccorsoDAO(EntityManager entityManager) {
        super(entityManager);
    }

    public List<RichiestaSoccorso> findByStato(StatoRichiesta stato) {
        TypedQuery<RichiestaSoccorso> query = entityManager.createQuery(
                "SELECT r FROM RichiestaSoccorso r WHERE r.stato = :stato ORDER BY r.dataOraRichiesta DESC", 
                RichiestaSoccorso.class
        );
        query.setParameter("stato", stato);
        return query.getResultList();
    }

    public RichiestaSoccorso findByCodiceConvalida(String codice) {
        TypedQuery<RichiestaSoccorso> query = entityManager.createQuery(
                "SELECT r FROM RichiestaSoccorso r WHERE r.codiceConvalida = :codice", 
                RichiestaSoccorso.class
        );
        query.setParameter("codice", codice);
        List<RichiestaSoccorso> results = query.getResultList();
        return results.isEmpty() ? null : results.get(0);
    }

    public long countRecentRequests(String email, int minutes) {
        TypedQuery<Long> query = entityManager.createQuery(
                "SELECT COUNT(r) FROM RichiestaSoccorso r WHERE r.contattoEmail = :email AND r.dataOraRichiesta > :limit", 
                Long.class
        );
        query.setParameter("email", email);
        query.setParameter("limit", java.time.LocalDateTime.now().minusMinutes(minutes));
        return query.getSingleResult();
    }
}
