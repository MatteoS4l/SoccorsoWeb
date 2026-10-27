# SoccorsoWeb

Emergency-response management system, built for the *Web Engineering* and *Advanced Web Development* exams
(University of L'Aquila, 2026). Individual project.

The same domain is implemented twice:

| Folder | What it is |
|---|---|
| `SoccorsoWeb/` | Server-rendered web application (Jakarta EE 10: Servlets, FreeMarker templates, Hibernate/JPA) |
| `SoccorsoWebREST/` | REST API (Jersey / JAX-RS) with a JavaScript (Fetch API) + Bootstrap 5 client |
| `openapi.yaml` | OpenAPI 3 description of the REST API |
| `docs/` | ER diagram and navigation model |

## Features

Both versions implement the 13 required use cases, for two roles (`ADMIN` and `OPERATORE`):

1. Login / logout
2. Submit a rescue request
3. Confirm a rescue request
4. List rescue requests, filtered by type
5. List closed requests with a not fully positive outcome
6. List the operators that are currently free
7. Create a mission
8. Close a mission in progress
9. Cancel a rescue request (admin)
10. Mission details
11. Rescue request details
12. Operator details
13. List the missions an operator has taken part in

The server-rendered version also sends a confirmation link by e-mail, accepts a photo of the incident and uses a captcha on the request form.
Passwords are stored hashed (Spring Security).

## Tech stack

Java 17, Jakarta EE 10, Hibernate ORM 6.4, MySQL (Connector/J 8.3), FreeMarker 2.3 (server-rendered app),
Jersey 3.1 + Jackson 2.15 (REST API), Jakarta Mail, Spring Security Core (password hashing), Bootstrap 5, Maven (WAR packaging).

## Running locally

1. **Database**: MySQL on `localhost:3306` with a database called `soccorso_web`. SQL scripts are in `SoccorsoWeb/DB/`
   (`init.sql`, `dump.sql`); Hibernate is also configured with `hbm2ddl.auto=update`.
   The default connection (user `root`, empty password) is defined in `META-INF/persistence.xml` and is meant for local development only.
2. **E-mail** (server-rendered app only): set the environment variables `SOCCORSOWEB_SMTP_USER` and `SOCCORSOWEB_SMTP_PASS`
   (for Gmail, an app password). The SMTP host is configured in `EmailService.java`.
3. **Build**: run `mvn clean package` in `SoccorsoWeb/` and/or `SoccorsoWebREST/`, then deploy the WAR to a Jakarta EE 10 servlet container
   such as Apache Tomcat 10.1. The REST API is served under `/SoccorsoWebREST/api`.

## Notes

University project: configuration and security are simplified and it is not meant for production use.
The user interface and the original exam documentation are in Italian.

---

## Italiano

Sistema di gestione delle emergenze realizzato per gli esami di Ingegneria del Web e Sviluppo Web Avanzato (Università dell'Aquila, 2026),
progetto individuale. Due implementazioni dello stesso dominio: un'applicazione Jakarta EE con rendering lato server (`SoccorsoWeb/`)
e un'API REST con client JavaScript/Bootstrap (`SoccorsoWebREST/`, specifica in `openapi.yaml`).
Per l'invio delle e-mail di conferma servono le variabili d'ambiente `SOCCORSOWEB_SMTP_USER` e `SOCCORSOWEB_SMTP_PASS`.
