USE soccorso_web;

-- Pulisci i dati esistenti
SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE Utente_Skill;
TRUNCATE TABLE Utente_Patente;
TRUNCATE TABLE LogMissione;
TRUNCATE TABLE Missione_Materiale;
TRUNCATE TABLE Missione_Mezzo;
TRUNCATE TABLE Missione_Operatore;
TRUNCATE TABLE Missione;
TRUNCATE TABLE Materiale;
TRUNCATE TABLE Mezzo;
TRUNCATE TABLE RichiestaSoccorso;
TRUNCATE TABLE Skill;
TRUNCATE TABLE Patente;
TRUNCATE TABLE Utente;
SET FOREIGN_KEY_CHECKS = 1;

--  UTENTI
INSERT INTO Utente (email, password, nome, cognome, ruolo) VALUES
('admin@soccorso.it', '$2a$10$z5C..8rKfZsMiWPZwadetuREXw0XQIGvUyf4jrDwisN3pG53PW3RO', 'Marco', 'Rossi', 'ADMIN'),
('admin2@soccorso.it', '$2a$10$z5C..8rKfZsMiWPZwadetuREXw0XQIGvUyf4jrDwisN3pG53PW3RO', 'Matteo', 'Salvatore', 'ADMIN'),
('m.bianchi@soccorso.it', '$2a$10$z5C..8rKfZsMiWPZwadetuREXw0XQIGvUyf4jrDwisN3pG53PW3RO', 'Mario', 'Bianchi', 'OPERATORE'),
('l.verdi@soccorso.it', '$2a$10$z5C..8rKfZsMiWPZwadetuREXw0XQIGvUyf4jrDwisN3pG53PW3RO', 'Luca', 'Verdi', 'OPERATORE'),
('g.neri@soccorso.it', '$2a$10$z5C..8rKfZsMiWPZwadetuREXw0XQIGvUyf4jrDwisN3pG53PW3RO', 'Giulia', 'Neri', 'OPERATORE'),
('a.bruni@soccorso.it', '$2a$10$z5C..8rKfZsMiWPZwadetuREXw0XQIGvUyf4jrDwisN3pG53PW3RO', 'Anna', 'Bruni', 'OPERATORE'),
('s.gialli@soccorso.it', '$2a$10$z5C..8rKfZsMiWPZwadetuREXw0XQIGvUyf4jrDwisN3pG53PW3RO', 'Stefano', 'Gialli', 'OPERATORE'),
('c.moro@soccorso.it', '$2a$10$z5C..8rKfZsMiWPZwadetuREXw0XQIGvUyf4jrDwisN3pG53PW3RO', 'Chiara', 'Moro', 'OPERATORE');

--  PATENTI
INSERT INTO Patente (tipologia) VALUES ('B'), ('C'), ('D'), ('E'), ('Nautica'), ('Volo Elicottero');

--  SKILL
INSERT INTO Skill (descrizione) VALUES 
('BLS-D (Rianimazione)'), ('Soccorso Alpino'), ('Antincendio Boschivo'), 
('Gestione Sostanze Pericolose'), ('Sommozzatore'), ('Psicologia dell''Emergenza');

-- PATENTI UTENTE, SKILL UTENTE
INSERT INTO Utente_Patente (utente_id, patente_id) VALUES (3, 2), (4, 3), (5, 4), (6, 5), (7, 6), (8, 1);
INSERT INTO Utente_Skill (utente_id, skill_id) VALUES (3, 2), (4, 3), (5, 5), (6, 4), (7, 6), (8, 1), (8, 3);

--  MEZZI
INSERT INTO Mezzo (tipo, descrizione, disponibilita) VALUES
('Ambulanza A1', 'Unità mobile rianimazione', 'SI'),
('Ambulanza A2', 'Trasporto infermi standard', 'SI'),
('Auto Medica', 'Risposta rapida medico a bordo', 'SI'),
('Elicottero EC135', 'Elisoccorso avanzato', 'SI'),
('Fuoristrada 4x4', 'Land Rover Defender soccorso tecnico', 'SI'),
('Camion Idropulitrice', 'Gestione allagamenti e fango', 'SI'),
('Gommone Soccorso', 'Zodiac 450 per acque interne', 'SI');

--  MATERIALI
INSERT INTO Materiale (tipo, descrizione, quantita_disponibile) VALUES
('Kit Primo Soccorso', 'Borsa medica completa', 20),
('Defibrillatore DAE', 'Zoll AED 3', 8),
('Barelle Cucchiaio', 'Sistema spinale rigido', 12),
('Estintori CO2', 'Estintore 5kg carrellato', 15),
('Tuta Antiacido', 'Protezione chimica Livello B', 5),
('Cordame Soccorso', 'Corda statica 50m', 10),
('Radio Portatile', 'Motorola TETRA professionale', 25);

--  RICHIESTE DI SOCCORSO
INSERT INTO RichiestaSoccorso (nome_richiedente, contatto_email, indirizzo, descrizione, tipologia, stato, priorita, codice_convalida, foto) VALUES
('Francesco Rossi', 'francesco.rossi@email.it', 'Via Roma 12, Genova', 'Incidente tra due auto, fumo dal motore.', 'INCIDENTE_STRADALE', 'IN_ATTESA', 'URGENTE', 'XJ7K2L', 'uploads/foto0.png'),
('Giulia Bianchi', 'giulia.bianchi@email.it', 'Via Mare 5, Genova', 'Barca alla deriva con motore in avaria.', 'SALVATAGGIO', 'IN_ATTESA', 'ALTA', 'B9P4M1', 'uploads/foto1.jpg'),
('Luca Verdi', 'luca.verdi@email.it', 'Via Firenze 66, Firenze', 'Forte odore di gas nel seminterrato.', 'FUGA_GAS', 'DA_CONVALIDARE', 'MEDIA', 'R3W8N5', 'uploads/foto2.jpg'),
('Anna Bruni', 'anna.bruni@email.it', 'Piazza Duomo 1, Milano', 'Sospetto attacco cardiaco.', 'MALORE', 'CONFERMATA', 'URGENTE', 'V2T9Q8', 'uploads/foto3.jpg'),
('Stefano Gialli', 'stefano.gialli@email.it', 'Via Quota 22, Pisa', 'Infortunio in quota.', 'INFORTUNIO_LAVORO', 'CONFERMATA', 'ALTA', 'H4X1Z3', NULL),
('Chiara Moro', 'chiara.moro@email.it', 'Via Seta 10, Venezia', 'Allagamento magazzino storico.', 'ALLUVIONE', 'CHIUSA', 'MEDIA', 'Y7C2K9', NULL),
('Mario Neri', 'mario.neri@email.it', 'Piazza Volta 1, Como', 'Cortocircuito quadro elettrico industriale.', 'GUASTO_INFRASTRUTTURA', 'IN_ATTESA', 'ALTA', 'L0M6B2', NULL),
('Elena Ferri', 'elena.ferri@email.it', 'Piazza Sabato 22, Recanati', 'Tentativo di furto con aggressione.', 'AGGRESSIONE', 'DA_CONVALIDARE', 'URGENTE', 'G5D3S8', NULL),
('Simone Neri', 'simone.neri@email.it', 'Via Bambini 5, Roma', 'Fuga di gas in locale scolastico.', 'FUGA_GAS', 'RIFIUTATA', 'MEDIA', 'F1P9R4', NULL),
('Giovanni Moro', 'giovanni.moro@email.it', 'Via Panisperna 89, Roma', 'Sversamento liquido sospetto in laboratorio.', 'INQUINAMENTO_AMBIENTALE', 'IN_ATTESA', 'ALTA', 'T6J2V7', NULL),
('Martina Bruni', 'martina.bruni@email.it', 'Corso Francia 100, Torino', 'Persona anziana caduta in casa con sospetta frattura.', 'INCIDENTE_DOMESTICO', 'IN_ATTESA', 'MEDIA', 'K8M1A3', NULL);

--  MISSIONI

INSERT INTO Missione (richiesta_id, admin_id, caposquadra_id, obiettivo, stato) VALUES
(5, 1, 3, 'Recupero tecnico in quota e stabilizzazione ferito.', 'ATTIVA');

INSERT INTO Missione (richiesta_id, admin_id, caposquadra_id, obiettivo, stato, livello_successo, note, data_ora_fine) VALUES
(6, 1, 4, 'Drenaggio magazzino.', 'COMPLETATA', 2, 'Intervento parziale: motopompa in avaria dopo 1 ora.', '2026-02-11 10:00:00');

--  ASSEGNAZIONI

INSERT INTO Missione_Operatore (missione_id, operatore_id) VALUES (1, 3), (1, 5);
INSERT INTO Missione_Mezzo (missione_id, mezzo_id) VALUES (1, 5);
UPDATE Mezzo SET disponibilita = 'NO' WHERE id = 5;
INSERT INTO Missione_Materiale (missione_id, materiale_id) VALUES (1, 6);

INSERT INTO Missione_Operatore (missione_id, operatore_id) VALUES (2, 4), (2, 6);
INSERT INTO Missione_Mezzo (missione_id, mezzo_id) VALUES (2, 6);

INSERT INTO LogMissione (missione_id, operatore_id, descrizione) VALUES
(1, 3, 'Arrivati sul posto. Predisposizione imbraghi.'),
(1, 3, 'Contatto visivo stabilito. Il ferito è cosciente.'),
(2, 4, 'Iniziate operazioni di drenaggio.'),
(2, 4, 'Avaria motopompa principale. Richiesto supporto tecnico.'),
(2, 1, 'Supporto non disponibile. Chiusura anticipata missione.');
