package com.soccorsoweb.data;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class DAOFactory {

    private static final String PERSISTENCE_UNIT_NAME = "soccorso_web_pu";
    private static EntityManagerFactory entityManagerFactory;

    private DAOFactory() {
        // Costruttore privato per evitare l'instanziazione
    }

    public static void init() {
        if (entityManagerFactory == null) {
            try {
                entityManagerFactory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
                System.out.println("EntityManagerFactory inizializzata con successo.");
            } catch (Exception e) {
                System.err.println("Errore durante l'inizializzazione dell'EntityManagerFactory: " + e.getMessage());
                e.printStackTrace();
                throw new ExceptionInInitializerError(e);
            }
        }
    }

    public static EntityManager getEntityManager() {
        if (entityManagerFactory == null) {
            init(); // Inizializza se non è già stato fatto
        }
        return entityManagerFactory.createEntityManager();
    }

    public static void close() {
        if (entityManagerFactory != null && entityManagerFactory.isOpen()) {
            entityManagerFactory.close();
            entityManagerFactory = null;
            System.out.println("EntityManagerFactory chiusa con successo.");
        }
    }

    // metodi specifici che creano un nuovo em
    
    public static UtenteDAO getUtenteDAO() {
        return new UtenteDAO(getEntityManager());
    }

    public static RichiestaSoccorsoDAO getRichiestaSoccorsoDAO() {
        return new RichiestaSoccorsoDAO(getEntityManager());
    }

    public static MissioneDAO getMissioneDAO() {
        return new MissioneDAO(getEntityManager());
    }

    public static MezzoDAO getMezzoDAO() {
        return new MezzoDAO(getEntityManager());
    }

    public static MaterialeDAO getMaterialeDAO() {
        return new MaterialeDAO(getEntityManager());
    }

    public static LogMissioneDAO getLogMissioneDAO() {
        return new LogMissioneDAO(getEntityManager());
    }

    public static PatenteDAO getPatenteDAO() {
        return new PatenteDAO(getEntityManager());
    }

    public static SkillDAO getSkillDAO() {
        return new SkillDAO(getEntityManager());
    }

    // metodi che usano un em già esistente

    public static UtenteDAO getUtenteDAO(EntityManager em) {
        return new UtenteDAO(em);
    }

    public static RichiestaSoccorsoDAO getRichiestaSoccorsoDAO(EntityManager em) {
        return new RichiestaSoccorsoDAO(em);
    }

    public static MissioneDAO getMissioneDAO(EntityManager em) {
        return new MissioneDAO(em);
    }

    public static MezzoDAO getMezzoDAO(EntityManager em) {
        return new MezzoDAO(em);
    }

    public static MaterialeDAO getMaterialeDAO(EntityManager em) {
        return new MaterialeDAO(em);
    }

    public static LogMissioneDAO getLogMissioneDAO(EntityManager em) {
        return new LogMissioneDAO(em);
    }

    public static PatenteDAO getPatenteDAO(EntityManager em) {
        return new PatenteDAO(em);
    }

    public static SkillDAO getSkillDAO(EntityManager em) {
        return new SkillDAO(em);
    }

    // metodi per le servlet

    private static EntityManager getEmFromRequest(jakarta.servlet.http.HttpServletRequest request) {
        return (EntityManager) request.getAttribute("jakarta.persistence.EntityManager");
    }

    public static UtenteDAO getUtenteDAO(jakarta.servlet.http.HttpServletRequest request) {
        return new UtenteDAO(getEmFromRequest(request));
    }

    public static RichiestaSoccorsoDAO getRichiestaSoccorsoDAO(jakarta.servlet.http.HttpServletRequest request) {
        return new RichiestaSoccorsoDAO(getEmFromRequest(request));
    }

    public static MissioneDAO getMissioneDAO(jakarta.servlet.http.HttpServletRequest request) {
        return new MissioneDAO(getEmFromRequest(request));
    }

    public static MezzoDAO getMezzoDAO(jakarta.servlet.http.HttpServletRequest request) {
        return new MezzoDAO(getEmFromRequest(request));
    }

    public static MaterialeDAO getMaterialeDAO(jakarta.servlet.http.HttpServletRequest request) {
        return new MaterialeDAO(getEmFromRequest(request));
    }

    public static LogMissioneDAO getLogMissioneDAO(jakarta.servlet.http.HttpServletRequest request) {
        return new LogMissioneDAO(getEmFromRequest(request));
    }

    public static PatenteDAO getPatenteDAO(jakarta.servlet.http.HttpServletRequest request) {
        return new PatenteDAO(getEmFromRequest(request));
    }

    public static SkillDAO getSkillDAO(jakarta.servlet.http.HttpServletRequest request) {
        return new SkillDAO(getEmFromRequest(request));
    }
}