package com.soccorsoweb.filter;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebFilter(filterName = "TransactionFilter", urlPatterns = "/*")
public class TransactionFilter implements Filter {

    private static final Logger LOGGER = Logger.getLogger(TransactionFilter.class.getName());
    private EntityManagerFactory emf;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Il metodo init recupera la factory creata dal Listener
        this.emf = (EntityManagerFactory) filterConfig.getServletContext().getAttribute("entityManagerFactory");
        if (this.emf == null) {
            throw new ServletException("EntityManagerFactory not found in ServletContext. Check SoccorsoWebListener.");
        }
        LOGGER.log(Level.INFO, "TransactionFilter initialized and retrieved EntityManagerFactory from ServletContext.");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        if (request instanceof HttpServletRequest) {
            HttpServletRequest httpRequest = (HttpServletRequest) request;
            String path = httpRequest.getRequestURI().substring(httpRequest.getContextPath().length());

            // Se la richiesta è per una risorsa statica, salta tutta la logica di transazione.
            if (path.endsWith(".css") || path.endsWith(".js") || path.endsWith(".png") || path.endsWith(".jpg")) {
                chain.doFilter(request, response);
                return;
            }
        }

        EntityManager em = null;
        try {
            em = emf.createEntityManager(); // Usa la factory recuperata
            em.getTransaction().begin();

            request.setAttribute("jakarta.persistence.EntityManager", em);

            chain.doFilter(request, response);

            if (em.getTransaction().isActive() && !em.getTransaction().getRollbackOnly()) {
                em.getTransaction().commit();
            } else if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Transaction rolled back due to an exception: " + e.getMessage(), e);
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new ServletException("Error during transaction processing", e);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public void destroy() {
        // La responsabilità di chiudere la factory è del Listener
        this.emf = null;
    }
}