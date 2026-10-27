package com.soccorsoweb.listener;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebListener
public class SoccorsoWebListener implements ServletContextListener {

    private static final Logger LOGGER = Logger.getLogger(SoccorsoWebListener.class.getName());

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        // Inizializza EntityManagerFactory
        LOGGER.log(Level.INFO, "Initializing EntityManagerFactory for SoccorsoWebREST...");
        try {
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("soccorso_web_pu");
            sce.getServletContext().setAttribute("entityManagerFactory", emf); // Salva la factory nel contesto globale
            LOGGER.log(Level.INFO, "EntityManagerFactory initialized and stored in ServletContext.");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error during EntityManagerFactory initialization", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        LOGGER.log(Level.INFO, "Application shutting down.");

        // Chiudi EntityManagerFactory
        Object emfAttribute = sce.getServletContext().getAttribute("entityManagerFactory");
        if (emfAttribute instanceof EntityManagerFactory) {
            EntityManagerFactory emf = (EntityManagerFactory) emfAttribute;
            if (emf.isOpen()) {
                emf.close();
                LOGGER.log(Level.INFO, "EntityManagerFactory closed.");
            }
        }

        // Non serve la chiusura per FreeMarker
    }
}