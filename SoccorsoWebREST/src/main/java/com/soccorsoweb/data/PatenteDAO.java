package com.soccorsoweb.data;

import com.soccorsoweb.model.Patente;
import jakarta.persistence.EntityManager;

public class PatenteDAO extends BaseDAO<Patente, Integer> {
    public PatenteDAO(EntityManager em) {
        super(em);
    }
}