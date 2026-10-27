
CREATE DATABASE IF NOT EXISTS soccorso_web;

USE soccorso_web;
-- 2. PULIZIA TOTALE (Disattiviamo i controlli per cancellare senza errori)
SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS Utente_Skill;
DROP TABLE IF EXISTS Utente_Patente;
DROP TABLE IF EXISTS LogMissione;
DROP TABLE IF EXISTS Missione_Materiale;
DROP TABLE IF EXISTS Missione_Mezzo;
DROP TABLE IF EXISTS Missione_Operatore;
DROP TABLE IF EXISTS Missione;
DROP TABLE IF EXISTS Materiale;
DROP TABLE IF EXISTS Mezzo;
DROP TABLE IF EXISTS RichiestaSoccorso;
DROP TABLE IF EXISTS Skill;
DROP TABLE IF EXISTS Patente;
DROP TABLE IF EXISTS Utente;

SET FOREIGN_KEY_CHECKS = 1;

CREATE TABLE IF NOT EXISTS Utente (
    id INT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    nome VARCHAR(255),
    cognome VARCHAR(255),
    ruolo ENUM('ADMIN', 'OPERATORE') NOT NULL
);
CREATE TABLE IF NOT EXISTS Patente (
    id INT AUTO_INCREMENT PRIMARY KEY,
    tipologia VARCHAR (50) NOT NULL UNIQUE
);
CREATE TABLE IF NOT EXISTS Skill (
    id INT AUTO_INCREMENT PRIMARY KEY,
    descrizione VARCHAR(100)
);
CREATE TABLE IF NOT EXISTS RichiestaSoccorso (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome_richiedente VARCHAR(255),
    data_ora_richiesta TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    indirizzo VARCHAR(500) NOT NULL,
    descrizione TEXT,
    contatto_email VARCHAR(255) NOT NULL,
    codice_convalida VARCHAR(100),
    foto VARCHAR(255),
    tipologia ENUM(
        'INCIDENTE_STRADALE',
        'INCIDENTE_DOMESTICO',
        'INFORTUNIO_LAVORO',
        'MALORE',
        'INCENDIO',
        'FUGA_GAS',
        'ALLUVIONE',
        'TERREMOTO',
        'FRANA',
        'VALANGA',
        'RICERCA_DISPERSO',
        'SALVATAGGIO',
        'INQUINAMENTO_AMBIENTALE',
        'GUASTO_INFRASTRUTTURA',
        'CROLLO_EDIFICIO',
        'MINACCIA_BOMBA',
        'RAPINA',
        'AGGRESSIONE',
        'GUASTO_CALDAIA',
        'GUASTO_ELETTRODOMESTICO',
        'ALTRO'
    ) NOT NULL DEFAULT 'ALTRO',
    stato ENUM(
        'DA_CONVALIDARE',
        'IN_ATTESA',
        'CONFERMATA',
        'CHIUSA',
        'RIFIUTATA'
    ) DEFAULT 'DA_CONVALIDARE',
    priorita ENUM('BASSA', 'MEDIA', 'ALTA', 'URGENTE') DEFAULT 'MEDIA'
);
CREATE TABLE IF NOT EXISTS Mezzo (
    id INT AUTO_INCREMENT PRIMARY KEY,
    tipo VARCHAR(50),
    descrizione VARCHAR(100),
    disponibilita ENUM ('SI', 'NO') NOT NULL
    -- bisogna settare a no quando viene inserito in una missione
);
CREATE TABLE IF NOT EXISTS Materiale (
    id INT AUTO_INCREMENT PRIMARY KEY,
    tipo VARCHAR(50),
    descrizione VARCHAR(100),
    quantita_disponibile INT NOT NULL
);
CREATE TABLE IF NOT EXISTS Missione (
    id INT AUTO_INCREMENT PRIMARY KEY,
    richiesta_id INT NOT NULL UNIQUE,
    admin_id INT NOT NULL,
    caposquadra_id INT,
    obiettivo TEXT,
    -- lo inserirà l'admin per evitare i typo nella richiesta
    data_ora_inizio TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_ora_fine TIMESTAMP NULL,
    -- lo facciamo inserire dal caposquadra
    livello_successo INT CHECK (livello_successo BETWEEN 1 AND 5),
    stato ENUM('ATTIVA', 'COMPLETATA', 'ANNULLATA') DEFAULT 'ATTIVA',
    note TEXT,
    FOREIGN KEY (richiesta_id) REFERENCES RichiestaSoccorso(id) ON DELETE CASCADE,
    FOREIGN KEY (admin_id) REFERENCES Utente(id) ON DELETE RESTRICT,
    FOREIGN KEY (caposquadra_id) REFERENCES Utente(id) ON DELETE
    SET NULL
);
CREATE TABLE IF NOT EXISTS Missione_Operatore (
    missione_id INT,
    operatore_id INT,
    PRIMARY KEY (missione_id, operatore_id),
    FOREIGN KEY (missione_id) REFERENCES Missione(id) ON DELETE CASCADE,
    FOREIGN KEY (operatore_id) REFERENCES Utente(id) ON DELETE CASCADE
);
CREATE TABLE IF NOT EXISTS Missione_Mezzo (
    missione_id INT,
    mezzo_id INT,
    PRIMARY KEY (missione_id, mezzo_id),
    FOREIGN KEY (missione_id) REFERENCES Missione(id) ON DELETE CASCADE,
    FOREIGN KEY (mezzo_id) REFERENCES Mezzo(id) ON DELETE RESTRICT
);
CREATE TABLE IF NOT EXISTS Missione_Materiale (
    missione_id INT,
    materiale_id INT,
    PRIMARY KEY (missione_id, materiale_id),
    FOREIGN KEY (missione_id) REFERENCES Missione(id) ON DELETE CASCADE,
    FOREIGN KEY (materiale_id) REFERENCES Materiale(id) ON DELETE RESTRICT
);
CREATE TABLE IF NOT EXISTS LogMissione (
    id INT AUTO_INCREMENT PRIMARY KEY,
    missione_id INT NOT NULL,
    operatore_id INT,
    -- Può essere NULL se è un log di sistema
    data_ora TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    descrizione TEXT NOT NULL,
    FOREIGN KEY (missione_id) REFERENCES Missione(id) ON DELETE CASCADE,
    FOREIGN KEY (operatore_id) REFERENCES Utente(id) ON DELETE
    SET NULL
);
CREATE TABLE IF NOT EXISTS Utente_Patente(
    utente_id INT,
    patente_id INT,
    PRIMARY KEY (utente_id, patente_id),
    FOREIGN KEY (utente_id) REFERENCES Utente(id) ON DELETE CASCADE,
    FOREIGN KEY (patente_id) REFERENCES Patente(id) ON DELETE CASCADE
);
CREATE TABLE IF NOT EXISTS Utente_Skill(
    utente_id INT,
    skill_id INT,
    PRIMARY KEY (utente_id, skill_id),
    FOREIGN KEY (utente_id) REFERENCES Utente(id) ON DELETE CASCADE,
    FOREIGN KEY (skill_id) REFERENCES Skill(id) ON DELETE CASCADE
);