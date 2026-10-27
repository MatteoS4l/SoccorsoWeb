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

    <#assign activePage = 'missioni'>
    <#include "includes/header.ftl">

    <div class="container">
        
        <div class="welcome-section" style="margin-bottom: 40px; margin-top: 20px;">
            <h1 style="font-size: 2rem; color: var(--primary-dark); margin-bottom: 5px;">Gestione Interventi</h1>
            <p style="color: #666;">Coordinamento e monitoraggio delle missioni attive e concluse.</p>
        </div>

        <main class="content">
            <!-- SEZIONE MISSIONI ATTIVE -->
            <section class="card" style="padding: 0; overflow: hidden; margin-bottom: 40px; border-left: 5px solid var(--primary-color);">
                <div style="padding: 20px 25px; border-bottom: 1px solid #eee; display: flex; justify-content: space-between; align-items: center; background: #fafafa;">
                    <h2 style="margin: 0; font-size: 1.2rem;">Missioni in Corso</h2>
                </div>

                <div style="padding: 0;">
                    <#if missioniAttive?has_content>
                        <table class="data-table">
                            <thead>
                                <tr>
                                    <th>ID</th>
                                    <th>Inizio</th>
                                    <th>Obiettivo</th>
                                    <th>Caposquadra</th>
                                    <th>Stato</th>
                                    <th style="text-align: right;">Operazioni</th>
                                </tr>
                            </thead>
                            <tbody>
                                <#list missioniAttive as m>
                                    <tr>
                                        <td><strong>#${m.id}</strong></td>
                                        <td style="color: #666;">${m.dataOraInizio?string?replace("T", ", ")}</td>
                                        <td><strong>${m.obiettivo}</strong></td>
                                        <td>
                                            <#if m.caposquadra??>
                                                ${m.caposquadra.nome} ${m.caposquadra.cognome}
                                            <#else>
                                                <em style="color: #aaa;">Non assegnato</em>
                                            </#if>
                                        </td>
                                        <td><span class="badge badge-alta">IN CORSO</span></td>
                                        <td style="text-align: right;">
                                            <a href="${contextPath}/dettaglio-missione?id=${m.id}" class="btn btn-small btn-primary">
                                                Gestisci
                                            </a>
                                        </td>
                                    </tr>
                                </#list>
                            </tbody>
                        </table>
                    <#else>
                        <div style="padding: 40px; text-align: center; color: #888;">
                            Nessuna missione operativa al momento.
                        </div>
                    </#if>
                </div>
            </section>

            <!-- STORICO MISSIONI -->
            <section class="card" style="padding: 0; overflow: hidden; opacity: 0.9;">
                <div style="padding: 20px 25px; border-bottom: 1px solid #eee; background: #fafafa;">
                    <h2 style="margin: 0; font-size: 1.2rem;">Archivio Interventi</h2>
                </div>

                <div style="padding: 0;">
                    <#if missioniConcluse?has_content>
                        <table class="data-table">
                            <thead>
                                <tr>
                                    <th>ID</th>
                                    <th>Data</th>
                                    <th>Obiettivo</th>
                                    <th>Esito</th>
                                    <th style="text-align: right;">Azioni</th>
                                </tr>
                            </thead>
                            <tbody>
                                <#list missioniConcluse as m>
                                    <tr>
                                        <td>#${m.id}</td>
                                        <td style="color: #666;">${m.dataOraInizio?string?replace("T", ", ")}</td>
                                        <td>${m.obiettivo}</td>
                                        <td>
                                            <span class="badge badge-${(m.stato == 'COMPLETATA')?string('bassa', 'urgente')}">
                                                ${m.stato}
                                            </span>
                                        </td>
                                        <td style="text-align: right;">
                                            <a href="${contextPath}/dettaglio-missione?id=${m.id}" class="btn btn-small btn-secondary">
                                                Visualizza
                                            </a>
                                        </td>
                                    </tr>
                                </#list>
                            </tbody>
                        </table>
                    <#else>
                        <div style="padding: 40px; text-align: center; color: #888;">L'archivio è attualmente vuoto.</div>
                    </#if>
                </div>
            </section>
        </main>

        <footer style="margin-top: 60px; padding-bottom: 40px; text-align: center; color: #bbb; font-size: 0.85rem;">
            <p>&copy; 2026 SoccorsoWeb - Registro Missioni</p>
        </footer>
    </div>

    <script src="${contextPath}/js/main.js"></script>
</body>
</html>