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

    <#assign activePage = 'dashboard'>
    <#include "includes/header.ftl">

    <div class="container">
        
        <div class="welcome-section" style="margin-bottom: 40px; margin-top: 20px;">
            <h1 style="font-size: 2rem; color: var(--primary-dark); margin-bottom: 5px;">Centrale Operativa</h1>
            <p style="color: #666;">Monitoraggio in tempo reale delle segnalazioni sul territorio.</p>
        </div>

        <!-- STATS SUMMARY -->
        <div class="stats-grid">
            <div class="stat-card">
                <h3>Richieste in Attesa</h3>
                <div class="value">${richieste?size}</div>
            </div>
            <div class="stat-card" style="border-left-color: var(--success);">
                <h3>Profilo Attivo</h3>
                <div class="value" style="font-size: 1.4rem; margin-top: 10px;">${user.ruolo}</div>
            </div>
            <div class="stat-card" style="border-left-color: #ff9800;">
                <h3>Data Odierna</h3>
                <div class="value" style="font-size: 1.4rem; margin-top: 10px;">${.now?string("dd/MM/yyyy")}</div>
            </div>
        </div>

        <!-- MAIN CONTENT: TABLE -->
        <main class="content">
            <section class="card" style="padding: 0; overflow: hidden;">
                <div style="padding: 20px 25px; border-bottom: 1px solid #eee; display: flex; justify-content: space-between; align-items: center; background: #fafafa;">
                    <h2 style="margin: 0; font-size: 1.2rem;">Segnalazioni in Ingresso</h2>
                </div>

                <div style="padding: 0;">
                    <#if richieste?has_content>
                        <table class="data-table" id="tableRichieste">
                            <thead>
                                <tr>
                                    <th style="cursor: pointer;" onclick="sortTable(0)">Ricevuta <i class="fas fa-sort"></i></th>
                                    <th style="cursor: pointer;" onclick="sortTable(1)">Richiedente <i class="fas fa-sort"></i></th>
                                    <th style="cursor: pointer;" onclick="sortTable(2)">Località <i class="fas fa-sort"></i></th>
                                    <th style="cursor: pointer;" onclick="sortTable(3)">Evento <i class="fas fa-sort"></i></th>
                                    <th style="cursor: pointer;" onclick="sortTable(4)">Priorità <i class="fas fa-sort"></i></th>
                                    <th style="text-align: right;">Operazioni</th>
                                </tr>
                            </thead>
                            <tbody>
                                <#list richieste as r>
                                    <tr>
                                        <td style="color: #666;" data-value="${r.dataOraRichiesta}">
                                            ${r.dataOraRichiesta?string?replace("T", ", ")}
                                        </td>
                                        <td><strong>${r.nomeRichiedente}</strong></td>
                                        <td><i class="fas fa-map-marker-alt" style="color: var(--primary-color); margin-right: 5px;"></i> ${r.indirizzo}</td>
                                        <td>
                                            <span style="font-size: 0.85rem; background: #eee; padding: 4px 10px; border-radius: 4px; font-weight: 600;">
                                                ${r.tipologia}
                                            </span>
                                        </td>
                                        <td data-value="${r.priorita}">
                                            <span class="badge badge-${r.priorita?lower_case}">
                                                ${r.priorita}
                                            </span>
                                        </td>
                                        <td style="text-align: right;">
                                            <a href="${contextPath}/gestione-richiesta?id=${r.id}" class="btn btn-small btn-primary">
                                                Gestisci
                                            </a>
                                        </td>
                                    </tr>
                                </#list>
                            </tbody>
                        </table>
                    <#else>
                        <div style="padding: 60px; text-align: center; color: #aaa;">
                            <i class="fas fa-check-circle" style="font-size: 3rem; margin-bottom: 15px; opacity: 0.3;"></i>
                            <h3 style="color: #888;">Nessun intervento pendente</h3>
                        </div>
                    </#if>
                </div>
            </section>
        </main>

        <footer style="margin-top: 60px; padding-bottom: 40px; text-align: center; color: #bbb; font-size: 0.85rem;">
            <p>&copy; 2026 SoccorsoWeb - Sistema Gestione Emergenze</p>
        </footer>
    </div>

    <script src="${contextPath}/js/main.js"></script>
    <script src="${contextPath}/js/filtroDashboard.js"></script>
</body>
</html>