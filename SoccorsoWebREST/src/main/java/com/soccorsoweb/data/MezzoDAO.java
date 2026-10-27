package com.soccorsoweb.data;

import com.soccorsoweb.model.Mezzo;
import com.soccorsoweb.model.enums.StatoMezzo;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;

public class MezzoDAO extends BaseDAO<Mezzo, Integer> {

    public MezzoDAO(EntityManager entityManager) {
        super(entityManager);
    }

    public List<Mezzo> findByStato(StatoMezzo stato) {
        TypedQuery<Mezzo> query = entityManager.createQuery(
                "SELECT m FROM Mezzo m WHERE m.disponibilita = :stato", 
                Mezzo.class
        );
        query.setParameter("stato", stato);
        return query.getResultList();
    }
}
