<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Dettaglio Missione - SoccorsoWeb</title>
    <link rel="stylesheet" href="${contextPath}/css/style.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
</head>
<body class="dashboard-body">

    <#assign activePage = 'missioni'>
    <#include "includes/header.ftl">

    <div class="container">
        
        <div class="welcome-section" style="margin-bottom: 40px; margin-top: 20px; display: flex; justify-content: space-between; align-items: flex-end;">
            <div>
                <a href="${contextPath}/missioni" style="text-decoration: none; color: var(--primary-color); font-weight: bold; font-size: 0.9rem; text-transform: uppercase;">
                    <i class="fas fa-arrow-left"></i> Elenco Missioni
                </a>
                <h1 style="font-size: 2rem; color: var(--primary-dark); margin-top: 10px;">Missione #${missione.id}</h1>
            </div>
            <div class="status-badge">
                <span class="badge badge-${(missione.stato == 'ATTIVA')?string('alta', (missione.stato == 'COMPLETATA')?string('bassa', 'urgente'))}" style="padding: 10px 20px; font-size: 1rem;">
                    STATO: ${missione.stato}
                </span>
            </div>
        </div>

        <main class="content">
            <div class="grid-2">
                <!-- COLONNA SINISTRA: INFO E RISORSE -->
                <div class="info-col">
                    <section class="card" style="margin-bottom: 25px;">
                        <h2 style="font-size: 1.2rem; margin-top: 0; color: var(--primary-color); border-bottom: 1px solid #eee; padding-bottom: 10px; margin-bottom: 20px;">
                            Obiettivo Operativo
                        </h2>
                        <p style="font-size: 1.1rem; font-weight: 500; line-height: 1.6; background: #f9f9f9; padding: 20px; border-radius: 6px; border-left: 5px solid var(--primary-color);">
                            ${missione.obiettivo}
                        </p>
                        
                        <div class="grid-2" style="margin-top: 25px;">
                            <div class="info-group">
                                <label>Inizio Operazioni</label>
                                <p style="color: #555;">${missione.dataOraInizio?string?replace("T", ", ")}</p>
                            </div>
                            <#if missione.dataOraFine??>
                                <div class="info-group">
                                    <label>Chiusura</label>
                                    <p style="color: #555;">${missione.dataOraFine?string?replace("T", ", ")}</p>
                                </div>
                            </#if>
                        </div>

                        <div class="info-group" style="margin-top: 20px;">
                            <label>Caposquadra Responsabile</label>
                            <p><strong>
                                <#if missione.caposquadra??>
                                    ${missione.caposquadra.nome} ${missione.caposquadra.cognome}
                                <#else>
                                    <em style="color: #aaa;">Non assegnato</em>
                                </#if>
                            </strong></p>
                        </div>
                    </section>

                    <section class="card">
                        <h2 style="font-size: 1.2rem; margin-top: 0; border-bottom: 1px solid #eee; padding-bottom: 10px; margin-bottom: 20px;">Risorse Assegnate</h2>
                        
                        <h3 style="font-size: 0.85rem; text-transform: uppercase; color: #888; margin-top: 10px; letter-spacing: 1px;">Mezzi di Soccorso</h3>
                        <div class="checkbox-list" style="max-height: 150px; background: #fafafa; margin-bottom: 20px; border: 1px solid #eee;">
                            <#list missione.mezzi as m>
                                <div style="padding: 10px; border-bottom: 1px solid #eee;">
                                    <strong>${m.tipo}</strong> <small style="color: #666; margin-left: 5px;">(${m.descrizione!""})</small>
                                </div>
                            </#list>
                        </div>

                        <h3 style="font-size: 0.85rem; text-transform: uppercase; color: #888; letter-spacing: 1px;">Personale in Squadra</h3>
                        <div class="checkbox-list" style="max-height: 150px; background: #fafafa; border: 1px solid #eee;">
                            <#list missione.operatori as o>
                                <div style="padding: 10px; border-bottom: 1px solid #eee;">
                                    <strong>${o.nome} ${o.cognome}</strong>
                                </div>
                            </#list>
                        </div>

                        <#if missione.stato == 'ATTIVA' && (user.ruolo == 'ADMIN' || (missione.caposquadra?? && missione.caposquadra.id == user.id))>
                            <div style="margin-top: 30px;">
                                <a href="${contextPath}/rientro-missione?id=${missione.id}" class="btn btn-danger btn-full" style="padding: 15px;">
                                    Concludi Intervento
                                </a>
                            </div>
                        </#if>
                    </section>
                </div>

                <!-- COLONNA DESTRA: DIARIO (LOG) -->
                <div class="log-col">
                    <section class="card" style="display: flex; flex-direction: column; height: 100%; min-height: 600px; padding: 0; overflow: hidden;">
                        <div style="padding: 20px 25px; border-bottom: 1px solid #eee; background: #fafafa;">
                            <h2 style="margin: 0; font-size: 1.2rem;">Diario di Bordo</h2>
                        </div>
                        
                        <div style="padding: 25px; flex-grow: 1; display: flex; flex-direction: column;">
                            <#if missione.stato == 'ATTIVA'>
                                <form action="${contextPath}/dettaglio-missione" method="POST" style="margin-bottom: 30px;">
                                    <input type="hidden" name="id" value="${missione.id}">
                                    <input type="hidden" name="action" value="addLog">
                                    <div class="form-group">
                                        <textarea name="descrizione" placeholder="Inserisci un aggiornamento sulle operazioni..." required style="height: 100px; resize: none;"></textarea>
                                    </div>
                                    <button type="submit" class="btn btn-primary btn-small">
                                        Invia Rapporto
                                    </button>
                                </form>
                                <hr style="border: 0; border-top: 1px solid #eee; margin-bottom: 25px;">
                            </#if>

                            <div class="log-container" style="flex-grow: 1; padding: 15px; background: #fcfcfc; border-radius: 8px; border: 1px solid #eee; overflow-y: auto;">
                                <#if logs?has_content>
                                    <#list logs as l>
                                        <div class="log-entry" style="background: white; padding: 15px; border-radius: 8px; box-shadow: 0 2px 5px rgba(0,0,0,0.03); margin-bottom: 15px; border-left: 4px solid #ddd;">
                                            <div style="display: flex; justify-content: space-between; margin-bottom: 8px;">
                                                <strong style="font-size: 0.9rem; color: var(--primary-color);">
                                                    ${l.operatore.nome} ${l.operatore.cognome}
                                                </strong>
                                                <small style="color: #bbb;">${l.dataOra?string?replace("T", ", ")}</small>
                                            </div>
                                            <p style="margin: 0; line-height: 1.5; color: #444; font-size: 0.95rem;">${l.descrizione}</p>
                                        </div>
                                    </#list>
                                <#else>
                                    <div style="text-align: center; color: #ccc; padding-top: 100px;">
                                        <i class="fas fa-clipboard-list" style="font-size: 3rem; margin-bottom: 15px; display: block; opacity: 0.2;"></i>
                                        Nessun rapporto inserito.
                                    </div>
                                </#if>
                            </div>
                        </div>
                    </section>
                </div>
            </div>
        </main>

        <footer style="margin-top: 60px; padding-bottom: 40px; text-align: center; color: #bbb; font-size: 0.85rem;">
            <p>&copy; 2026 SoccorsoWeb - Area Riservata Operativa</p>
        </footer>
    </div>

    <script src="${contextPath}/js/main.js"></script>
</body>
</html>
