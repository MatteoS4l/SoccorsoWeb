<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Gestione Risorse - SoccorsoWeb</title>
    <link rel="stylesheet" href="${contextPath}/css/style.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
</head>
<body class="dashboard-body">

    <#assign activePage = 'risorse'>
    <#include "includes/header.ftl">

    <div class="container">
        
        <#if successMessage??>
            <div class="alert alert-success">${successMessage}</div>
        </#if>
        <#if errorMessage??>
            <div class="alert alert-danger">${errorMessage}</div>
        </#if>

        <div class="welcome-section" style="margin-bottom: 40px; margin-top: 20px; display: flex; justify-content: space-between; align-items: flex-end;">
            <div>
                <h1 style="font-size: 2rem; color: var(--primary-dark); margin-bottom: 5px;">Amministrazione Risorse</h1>
                <p style="color: #666;">Gestione del personale, parco mezzi e inventario magazzino.</p>
            </div>
            <button onclick="openModal('modalUtente')" class="btn btn-primary">
                Nuovo Utente
            </button>
        </div>

        <main class="content">
            <!-- SEZIONE UTENTI -->
            <section class="card" style="padding: 0; overflow: hidden; margin-bottom: 40px;">
                <div style="padding: 20px 25px; border-bottom: 1px solid #eee; background: #fafafa;">
                    <h2 style="margin: 0; font-size: 1.2rem;">Personale Operativo</h2>
                </div>
                <table class="data-table">
                    <thead>
                        <tr>
                            <th>Nome e Cognome</th>
                            <th>Email / Username</th>
                            <th>Ruolo</th>
                            <th style="text-align: right;">Operazioni</th>
                        </tr>
                    </thead>
                    <tbody>
                        <#list utenti as u>
                            <tr>
                                <td><strong>${u.nome} ${u.cognome}</strong></td>
                                <td style="color: #666;">${u.email}</td>
                                <td>
                                    <span class="badge" style="background: ${(u.ruolo == 'ADMIN')?string('#333', '#eee')}; color: ${(u.ruolo == 'ADMIN')?string('white', 'black')};">
                                        ${u.ruolo}
                                    </span>
                                </td>
                                <td style="text-align: right;">
                                    <div style="display: flex; gap: 10px; justify-content: flex-end;">
                                        <a href="${contextPath}/modifica-risorsa?type=operatore&id=${u.id}" class="btn btn-small btn-secondary" title="Modifica">
                                            Modifica
                                        </a>
                                        <button onclick="confermaEliminazione('operatore', ${u.id})" class="btn btn-small btn-danger" title="Elimina">
                                            Elimina
                                        </button>
                                    </div>
                                </td>
                            </tr>
                        </#list>
                    </tbody>
                </table>
            </section>

            <div class="grid-2">
                <!-- SEZIONE MEZZI -->
                <section class="card" style="padding: 0; overflow: hidden;">
                    <div style="padding: 20px 25px; border-bottom: 1px solid #eee; display: flex; justify-content: space-between; align-items: center; background: #fafafa;">
                        <h2 style="margin: 0; font-size: 1.1rem;">Parco Mezzi</h2>
                        <button onclick="openModal('modalMezzo')" class="btn btn-small btn-primary">Aggiungi</button>
                    </div>
                    <table class="data-table">
                        <thead>
                            <tr>
                                <th>Tipo / Targa</th>
                                <th>Stato</th>
                                <th style="text-align: right;">Azioni</th>
                            </tr>
                        </thead>
                        <tbody>
                            <#list mezzi as m>
                                <tr>
                                    <td><strong>${m.tipo}</strong><br><small style="color:#888;">${m.descrizione}</small></td>
                                    <td>
                                        <span class="badge badge-${(m.disponibilita == 'SI')?string('bassa', 'urgente')}">
                                            ${(m.disponibilita == 'SI')?string('Disponibile', 'In Uso')}
                                        </span>
                                    </td>
                                    <td style="text-align: right;">
                                        <div style="display: flex; gap: 5px; justify-content: flex-end;">
                                            <a href="${contextPath}/modifica-risorsa?type=mezzo&id=${m.id}" class="btn btn-small btn-secondary">
                                                Modifica
                                            </a>
                                        </div>
                                    </td>
                                </tr>
                            </#list>
                        </tbody>
                    </table>
                </section>

                <!-- SEZIONE MATERIALI -->
                <section class="card" style="padding: 0; overflow: hidden;">
                    <div style="padding: 20px 25px; border-bottom: 1px solid #eee; display: flex; justify-content: space-between; align-items: center; background: #fafafa;">
                        <h2 style="margin: 0; font-size: 1.1rem;">Magazzino</h2>
                        <button onclick="openModal('modalMateriale')" class="btn btn-small btn-primary">Aggiungi</button>
                    </div>
                    <table class="data-table">
                        <thead>
                            <tr>
                                <th>Materiale</th>
                                <th>Disponibilità</th>
                                <th style="text-align: right;">Azioni</th>
                            </tr>
                        </thead>
                        <tbody>
                            <#list materiali as mat>
                                <tr>
                                    <td><strong>${mat.tipo}</strong></td>
                                    <td>
                                        <span style="font-weight: bold; color: ${(mat.quantitaDisponibile > 5)?string('var(--success)', 'var(--danger)')};">
                                            ${mat.quantitaDisponibile} unità
                                        </span>
                                    </td>
                                    <td style="text-align: right;">
                                        <a href="${contextPath}/modifica-risorsa?type=materiale&id=${mat.id}" class="btn btn-small btn-secondary">
                                            Modifica
                                        </a>
                                    </td>
                                </tr>
                            </#list>
                        </tbody>
                    </table>
                </section>
            </div>

            <!-- SEZIONE: PATENTI E SKILL -->
            <div class="grid-2" style="margin-top: 40px;">
                <!-- SEZIONE PATENTI -->
                <section class="card" style="padding: 0; overflow: hidden;">
                    <div style="padding: 20px 25px; border-bottom: 1px solid #eee; display: flex; justify-content: space-between; align-items: center; background: #fafafa;">
                        <h2 style="margin: 0; font-size: 1.1rem;">Anagrafica Patenti</h2>
                        <button onclick="openModal('modalNuovaPatente')" class="btn btn-small btn-primary">Aggiungi</button>
                    </div>
                    <div class="checkbox-list" style="height: 200px; background: white; border: none;">
                        <#list tutteLePatenti as p>
                            <div style="padding: 10px 20px; border-bottom: 1px solid #f0f0f0; display: flex; justify-content: space-between; align-items: center;">
                                <strong>${p.tipologia}</strong>
                                <div style="display: flex; gap: 10px; align-items: center;">
                                    <small style="color: #bbb;">ID: ${p.id}</small>
                                    <button onclick="confermaEliminazione('patente', ${p.id})" class="btn btn-small btn-danger" style="padding: 2px 8px;">&times;</button>
                                </div>
                            </div>
                        </#list>
                    </div>
                </section>

                <!-- SEZIONE SKILL -->
                <section class="card" style="padding: 0; overflow: hidden;">
                    <div style="padding: 20px 25px; border-bottom: 1px solid #eee; display: flex; justify-content: space-between; align-items: center; background: #fafafa;">
                        <h2 style="margin: 0; font-size: 1.1rem;">Abilità e Qualifiche</h2>
                        <button onclick="openModal('modalNuovaSkill')" class="btn btn-small btn-primary">Aggiungi</button>
                    </div>
                    <div class="checkbox-list" style="height: 200px; background: white; border: none;">
                        <#list tutteLeSkill as s>
                            <div style="padding: 10px 20px; border-bottom: 1px solid #f0f0f0; display: flex; justify-content: space-between; align-items: center;">
                                <strong>${s.descrizione}</strong>
                                <div style="display: flex; gap: 10px; align-items: center;">
                                    <small style="color: #bbb;">ID: ${s.id}</small>
                                    <button onclick="confermaEliminazione('skill', ${s.id})" class="btn btn-small btn-danger" style="padding: 2px 8px;">&times;</button>
                                </div>
                            </div>
                        </#list>
                    </div>
                </section>
            </div>
        </main>

        <footer style="margin-top: 60px; padding-bottom: 40px; text-align: center; color: #bbb; font-size: 0.85rem;">
            <p>&copy; 2026 SoccorsoWeb - Gestione Risorse</p>
        </footer>
    </div>

    <!-- POP-UP NUOVO UTENTE -->
    <div id="modalUtente" class="modal">
        <div class="modal-content">
            <div class="modal-header">
                <h2>Registra Nuovo Utente</h2>
                <span class="close-modal" onclick="closeModal('modalUtente')">&times;</span>
            </div>
            <div class="modal-body">
                <form action="${contextPath}/gestione-risorse" method="POST">
                    <input type="hidden" name="action" value="addOperatore">
                    <div class="grid-2">
                        <div class="form-group">
                            <label>Nome</label>
                            <input type="text" name="nome" required>
                        </div>
                        <div class="form-group">
                            <label>Cognome</label>
                            <input type="text" name="cognome" required>
                        </div>
                    </div>
                    <div class="form-group">
                        <label>Email (Username)</label>
                        <input type="email" name="email" required>
                    </div>
                    <div class="grid-2">
                        <div class="form-group">
                            <label>Password Iniziale</label>
                            <input type="password" name="password" required>
                        </div>
                        <div class="form-group">
                            <label>Conferma Password</label>
                            <input type="password" name="password_confirm" required>
                        </div>
                    </div>
                    <div class="form-group">
                        <label>Ruolo</label>
                        <select name="ruolo">
                            <option value="OPERATORE">Operatore</option>
                            <option value="ADMIN">Amministratore</option>
                        </select>
                    </div>

                    <div class="grid-2">
                        <div class="form-group">
                            <label>Patenti Conseguite</label>
                            <div class="checkbox-list" style="height: 120px;">
                                <#list tutteLePatenti as p>
                                    <div class="checkbox-item">
                                        <input type="checkbox" name="patentiIds" value="${p.id}" id="p_new_${p.id}">
                                        <label for="p_new_${p.id}">${p.tipologia}</label>
                                    </div>
                                </#list>
                            </div>
                        </div>
                        <div class="form-group">
                            <label>Abilità e Skill</label>
                            <div class="checkbox-list" style="height: 120px;">
                                <#list tutteLeSkill as s>
                                    <div class="checkbox-item">
                                        <input type="checkbox" name="skillsIds" value="${s.id}" id="s_new_${s.id}">
                                        <label for="s_new_${s.id}">${s.descrizione}</label>
                                    </div>
                                </#list>
                            </div>
                        </div>
                    </div>

                    <button type="submit" class="btn btn-primary btn-full" style="margin-top: 10px; padding: 15px;">Crea Utente</button>
                </form>
            </div>
        </div>
    </div>

    <!-- POP-UP NUOVO MEZZO -->
    <div id="modalMezzo" class="modal">
        <div class="modal-content">
            <div class="modal-header">
                <h2>Aggiungi Nuovo Mezzo</h2>
                <span class="close-modal" onclick="closeModal('modalMezzo')">&times;</span>
            </div>
            <div class="modal-body">
                <form action="${contextPath}/gestione-risorse" method="POST">
                    <input type="hidden" name="action" value="addMezzo">
                    <div class="form-group">
                        <label>Tipo di Mezzo</label>
                        <input type="text" name="tipo" required placeholder="Es. Ambulanza Tipo A">
                    </div>
                    <div class="form-group">
                        <label>Descrizione / Targa</label>
                        <input type="text" name="descrizione" placeholder="Es. Targa o ID interno">
                    </div>
                    <button type="submit" class="btn btn-primary btn-full" style="margin-top: 10px; padding: 15px;">Salva Mezzo</button>
                </form>
            </div>
        </div>
    </div>

    <!-- POP-UP NUOVO MATERIALE -->
    <div id="modalMateriale" class="modal">
        <div class="modal-content">
            <div class="modal-header">
                <h2>Nuovo Materiale a Magazzino</h2>
                <span class="close-modal" onclick="closeModal('modalMateriale')">&times;</span>
            </div>
            <div class="modal-body">
                <form action="${contextPath}/gestione-risorse" method="POST">
                    <input type="hidden" name="action" value="addMateriale">
                    <div class="form-group">
                        <label>Nome Materiale</label>
                        <input type="text" name="tipo" required>
                    </div>
                    <div class="form-group">
                        <label>Descrizione</label>
                        <input type="text" name="descrizione">
                    </div>
                    <div class="form-group">
                        <label>Quantità Iniziale</label>
                        <input type="number" name="quantita" value="1" min="0" required>
                    </div>
                    <button type="submit" class="btn btn-primary btn-full" style="margin-top: 10px; padding: 15px;">Aggiungi al Magazzino</button>
                </form>
            </div>
        </div>
    </div>

    <!-- MODALE NUOVA PATENTE -->
    <div id="modalNuovaPatente" class="modal">
        <div class="modal-content">
            <div class="modal-header">
                <h2>Nuova Tipologia Patente</h2>
                <span class="close-modal" onclick="closeModal('modalNuovaPatente')">&times;</span>
            </div>
            <div class="modal-body">
                <form action="${contextPath}/gestione-risorse" method="POST">
                    <input type="hidden" name="action" value="addPatente">
                    <div class="form-group">
                        <label>Codice/Nome Patente</label>
                        <input type="text" name="tipologia" required placeholder="Es. Patente B, Nautica...">
                    </div>
                    <button type="submit" class="btn btn-primary btn-full" style="padding: 15px;">Salva Patente</button>
                </form>
            </div>
        </div>
    </div>

    <!-- MODALE NUOVA SKILL -->
    <div id="modalNuovaSkill" class="modal">
        <div class="modal-content">
            <div class="modal-header">
                <h2>Nuova Abilità/Skill</h2>
                <span class="close-modal" onclick="closeModal('modalNuovaSkill')">&times;</span>
            </div>
            <div class="modal-body">
                <form action="${contextPath}/gestione-risorse" method="POST">
                    <input type="hidden" name="action" value="addSkill">
                    <div class="form-group">
                        <label>Descrizione Abilità</label>
                        <input type="text" name="descrizione" required placeholder="Es. BLSD, Soccorso Alpino...">
                    </div>
                    <button type="submit" class="btn btn-primary btn-full" style="padding: 15px;">Salva Abilità</button>
                </form>
            </div>
        </div>
    </div>

    <script src="${contextPath}/js/main.js"></script>
</body>
</html>