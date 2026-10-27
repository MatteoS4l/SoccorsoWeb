package com.soccorsoweb.data;

import com.soccorsoweb.model.Missione;
import com.soccorsoweb.model.enums.StatoMissione;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;

public class MissioneDAO extends BaseDAO<Missione, Integer> {

    public MissioneDAO(EntityManager entityManager) {
        super(entityManager);
    }

    public List<Missione> findByStato(StatoMissione stato) {
        TypedQuery<Missione> query = entityManager.createQuery(
                "SELECT m FROM Missione m WHERE m.stato = :stato", 
                Missione.class
        );
        query.setParameter("stato", stato);
        return query.getResultList();
    }

    public List<Missione> findByUser(Integer utenteId, StatoMissione stato) {
        TypedQuery<Missione> query = entityManager.createQuery(
                "SELECT DISTINCT m FROM Missione m LEFT JOIN m.operatori o " +
                "WHERE (m.caposquadra.id = :uid OR o.id = :uid) " +
                "AND m.stato = :stato", 
                Missione.class
        );
        query.setParameter("uid", utenteId);
        query.setParameter("stato", stato);
        return query.getResultList();
    }
}
