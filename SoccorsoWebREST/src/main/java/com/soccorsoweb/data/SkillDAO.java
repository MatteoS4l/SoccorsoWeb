package com.soccorsoweb.data;

import com.soccorsoweb.model.Skill;
import jakarta.persistence.EntityManager;

public class SkillDAO extends BaseDAO<Skill, Integer> {
    public SkillDAO(EntityManager em) {
        super(em);
    }
}