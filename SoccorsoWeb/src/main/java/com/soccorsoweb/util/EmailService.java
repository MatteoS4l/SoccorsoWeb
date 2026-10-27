package com.soccorsoweb.util;

import com.soccorsoweb.model.Missione;
import com.soccorsoweb.model.RichiestaSoccorso;
import com.soccorsoweb.model.Utente;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.util.Properties;
import java.util.Random;

public class EmailService {

    private static final String SMTP_HOST = "smtp.gmail.com";
    private static final String SMTP_PORT = "587";
    private static final String SMTP_USER = System.getenv("SOCCORSOWEB_SMTP_USER");
    private static final String SMTP_PASS = System.getenv("SOCCORSOWEB_SMTP_PASS");

    public static void inviaConfermaRichiesta(RichiestaSoccorso richiesta, String contextPath, String serverName, int serverPort) {
        String link = "http://" + serverName + ":" + serverPort + contextPath + "/convalida?token=" + richiesta.getCodiceConvalida();
        String subject = "Convalida la tua richiesta di soccorso - SoccorsoWeb";
        String body = "Ciao " + richiesta.getNomeRichiedente() + ",\n\n"
                + "Per confermare la richiesta clicca qui: " + link;
        sendEmail(richiesta.getContattoEmail(), subject, body);
    }

    public static void avvisaOperatoriMissione(Missione missione) {
        String subject = "NUOVA MISSIONE ASSEGNATA: #" + missione.getId();
        String body = "Hai una nuova missione: " + missione.getObiettivo();
        

        if (missione.getCaposquadra() != null) {
            sendEmail(missione.getCaposquadra().getEmail(), subject, "[CAPOSQUADRA] " + body);
        }
        
   
        for (Utente operatore : missione.getOperatori()) {
            sendEmail(operatore.getEmail(), subject, body);
        }
    }

    public static void notificaChiusuraMissione(Missione missione) {
        String subject = "Chiusura Missione #" + missione.getId();
        String body = "La missione relativa all'obiettivo '" + missione.getObiettivo() + "' è stata dichiarata CONCLUSA.\n"
                + "Tutte le risorse sono in fase di rientro alla base.";

 
        if (missione.getCaposquadra() != null) {
            sendEmail(missione.getCaposquadra().getEmail(), subject, body);
        }


        for (Utente operatore : missione.getOperatori()) {
            sendEmail(operatore.getEmail(), subject, body);
        }
    }

    public static void inviaCredenzialiUtente(Utente utente, String passwordChiara) {
        String subject = "Benvenuto in SoccorsoWeb - Le tue credenziali";
        String body = "Ciao " + utente.getNome() + ",\n\n"
                + "Ecco le tue credenziali di accesso:\n"
                + "Email: " + utente.getEmail() + "\n"
                + "Password: " + passwordChiara + "\n\n"
                + "Il team di SoccorsoWeb";
        sendEmail(utente.getEmail(), subject, body);
    }

    public static String generaCodiceConvalida() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder sb = new StringBuilder("SW-");
        Random rnd = new Random();
        for (int i = 0; i < 8; i++) {
            sb.append(chars.charAt(rnd.nextInt(chars.length())));
        }
        return sb.toString();
    }

    private static void sendEmail(String to, String subject, String body) {
 //per capire cosa sta succedendo
        System.out.println("DEBUG: Entrato in sendEmail reale");
        System.out.println("DEBUG: Destinatario -> " + to);

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", SMTP_HOST);
        props.put("mail.smtp.port", SMTP_PORT);
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(SMTP_USER, SMTP_PASS);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(SMTP_USER, "SoccorsoWeb"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);
            message.setText(body);

            System.out.println("DEBUG: Chiamata Transport.send...");
            Transport.send(message);
            System.out.println("DEBUG: INVIO COMPLETATO!");

        } catch (Exception e) {
            System.out.println("DEBUG: ERRORE DURANTE L'INVIO: " + e.getMessage());
            e.printStackTrace();
        }
    }
}