package com.soccorsoweb.data;

import com.soccorsoweb.model.LogMissione;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;

public class LogMissioneDAO extends BaseDAO<LogMissione, Integer> {

    public LogMissioneDAO(EntityManager entityManager) {
        super(entityManager);
    }

    public List<LogMissione> findByMissione(Integer missioneId) {
        TypedQuery<LogMissione> query = entityManager.createQuery(
                "SELECT l FROM LogMissione l WHERE l.missione.id = :missioneId ORDER BY l.dataOra DESC", 
                LogMissione.class
        );
        query.setParameter("missioneId", missioneId);
        return query.getResultList();
    }
}
