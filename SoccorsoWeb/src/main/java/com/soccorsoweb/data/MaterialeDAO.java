package com.soccorsoweb.data;

import com.soccorsoweb.model.Materiale;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;

public class MaterialeDAO extends BaseDAO<Materiale, Integer> {

    public MaterialeDAO(EntityManager entityManager) {
        super(entityManager);
    }

    public List<Materiale> findDisponibili() {
        TypedQuery<Materiale> query = entityManager.createQuery(
                "SELECT m FROM Materiale m WHERE m.quantitaDisponibile > 0", 
                Materiale.class
        );
        return query.getResultList();
    }
}
