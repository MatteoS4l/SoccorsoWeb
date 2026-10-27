<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${title} - SoccorsoWeb</title>
    <link rel="stylesheet" href="${contextPath}/css/style.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
</head>
<body class="dashboard-body">

    <#assign activePage = 'risorse'>
    <#include "includes/header.ftl">

    <div class="container" style="margin-top: 30px;">
        
        <#if successMessage??>
            <div class="alert alert-success">${successMessage}</div>
        </#if>
        <#if errorMessage??>
            <div class="alert alert-danger">${errorMessage}</div>
        </#if>
        
        <div class="welcome-section" style="margin-bottom: 30px;">
            <a href="${contextPath}/gestione-risorse" style="text-decoration: none; color: var(--primary-color); font-weight: bold;">
                <i class="fas fa-arrow-left"></i> Torna alla gestione risorse
            </a>
            <h1 style="font-size: 1.8rem; color: var(--text-dark); margin-top: 10px;">
                <#if risorsa.id == 0>Nuovo<#else>Modifica</#if> ${type?capitalize}
            </h1>
        </div>

        <main class="content">
            <section class="card" style="max-width: 700px; margin: 0 auto;">
                <form action="${contextPath}/modifica-risorsa" method="POST">
                    <input type="hidden" name="id" value="${risorsa.id}">
                    <input type="hidden" name="type" value="${type}">

                    <div style="background: #fcfcfc; padding: 20px; border-radius: 8px; border: 1px solid #eee; margin-bottom: 25px;">
                        <#if type == "operatore">
                            <h3 style="margin-top: 0; color: #555;"><i class="fas fa-user-edit"></i> Informazioni Personale</h3>
                            <div class="grid-2">
                                <div class="form-group">
                                    <label>Nome</label>
                                    <input type="text" name="nome" value="${risorsa.nome!''}" required placeholder="Es. Mario">
                                </div>
                                <div class="form-group">
                                    <label>Cognome</label>
                                    <input type="text" name="cognome" value="${risorsa.cognome!''}" required placeholder="Es. Rossi">
                                </div>
                            </div>
                            <div class="form-group">
                                <label>Email (Username)</label>
                                <input type="email" name="email" value="${risorsa.email!''}" required placeholder="mario.rossi@esempio.it">
                            </div>
                            <div class="form-group">
                                <label>Ruolo nel Sistema</label>
                                <select name="ruolo" required>
                                    <option value="OPERATORE" ${(risorsa.ruolo == 'OPERATORE')?string('selected', '')}>Operatore Soccorritore</option>
                                    <option value="ADMIN" ${(risorsa.ruolo == 'ADMIN')?string('selected', '')}>Amministratore di Sistema</option>
                                </select>
                            </div>

                            <div class="grid-2" style="margin-top: 25px;">
                                <div class="form-group">
                                    <label>Patenti Conseguite</label>
                                    <div class="checkbox-list" style="height: 150px; background: #fafafa;">
                                        <#list tutteLePatenti as p>
                                            <#assign hasPatente = false>
                                            <#if risorsa.patenti??>
                                                <#list risorsa.patenti as up>
                                                    <#if up.id == p.id><#assign hasPatente = true><#break></#if>
                                                </#list>
                                            </#if>
                                            <div class="checkbox-item">
                                                <label for="p_${p.id}">${p.tipologia}</label>
                                                <input type="checkbox" name="patentiIds" value="${p.id}" id="p_${p.id}" ${hasPatente?string('checked', '')}>
                                            </div>
                                        </#list>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label>Abilità e Skill</label>
                                    <div class="checkbox-list" style="height: 150px; background: #fafafa;">
                                        <#list tutteLeSkill as s>
                                            <#assign hasSkill = false>
                                            <#if risorsa.skills??>
                                                <#list risorsa.skills as us>
                                                    <#if us.id == s.id><#assign hasSkill = true><#break></#if>
                                                </#list>
                                            </#if>
                                            <div class="checkbox-item">
                                                <label for="s_${s.id}">${s.descrizione}</label>
                                                <input type="checkbox" name="skillsIds" value="${s.id}" id="s_${s.id}" ${hasSkill?string('checked', '')}>
                                            </div>
                                        </#list>
                                    </div>
                                </div>
                            </div>

                            <#if risorsa.id == 0>
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
                            <#else>
                                <div style="margin-top: 20px; padding-top: 15px; border-top: 1px dashed #ddd;">
                                    <p style="font-size: 0.85rem; color: #888; margin-bottom: 10px;">
                                        <i class="fas fa-key"></i> Lascia i campi vuoti se non desideri modificare la password attuale.
                                    </p>
                                    <div class="grid-2">
                                        <div class="form-group">
                                            <label>Nuova Password</label>
                                            <input type="password" name="password">
                                        </div>
                                        <div class="form-group">
                                            <label>Conferma Nuova Password</label>
                                            <input type="password" name="password_confirm">
                                        </div>
                                    </div>
                                </div>
                            </#if>
                        
                        <#elseif type == "mezzo">
                            <h3 style="margin-top: 0; color: #555;"><i class="fas fa-ambulance"></i> Dettagli Mezzo di Soccorso</h3>
                            <div class="form-group">
                                <label>Tipologia Mezzo</label>
                                <input type="text" name="tipo" value="${risorsa.tipo!''}" required placeholder="Es. Ambulanza Tipo A, Elicottero...">
                            </div>
                            <div class="form-group">
                                <label>Descrizione / Targa / Identificativo</label>
                                <input type="text" name="descrizione" value="${risorsa.descrizione!''}" placeholder="Es. Targa o nome del mezzo">
                            </div>

                        <#elseif type == "materiale">
                            <h3 style="margin-top: 0; color: #555;"><i class="fas fa-boxes"></i> Gestione Inventario Materiali</h3>
                            <div class="form-group">
                                <label>Nome Materiale</label>
                                <input type="text" name="tipo" value="${risorsa.tipo!''}" required placeholder="Es. Defibrillatore, Kit Bende...">
                            </div>
                            <div class="form-group">
                                <label>Descrizione</label>
                                <input type="text" name="descrizione" value="${risorsa.descrizione!''}" placeholder="Dettagli aggiuntivi sul materiale">
                            </div>
                            <div class="form-group">
                                <label>Quantità Attuale in Magazzino</label>
                                <input type="number" name="quantita" value="${risorsa.quantitaDisponibile!0}" required min="0">
                            </div>
                        </#if>
                    </div>

                    <div class="form-actions" style="display: flex; gap: 15px; justify-content: flex-end;">
                        <a href="${contextPath}/gestione-risorse" class="btn btn-secondary">Annulla</a>
                        <button type="submit" class="btn btn-primary">
                            <i class="fas fa-save"></i> Salva Informazioni
                        </button>
                    </div>
                </form>
            </section>
        </main>

        <footer style="margin-top: 50px; padding-bottom: 30px; text-align: center; color: #aaa; font-size: 0.85rem;">
            <p>&copy; 2026 SoccorsoWeb - Area Riservata</p>
        </footer>
    </div>

    <script src="${contextPath}/js/main.js"></script>
</body>
</html>