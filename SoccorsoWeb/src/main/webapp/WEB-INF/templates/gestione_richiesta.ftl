<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Gestione Richiesta #${richiesta.id} - SoccorsoWeb</title>
    <link rel="stylesheet" href="${contextPath}/css/style.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
</head>
<body class="dashboard-body">

    <#assign activePage = 'dashboard'>
    <#include "includes/header.ftl">

    <div class="container" style="margin-top: 30px;">
        
        <div class="welcome-section" style="margin-bottom: 40px;">
            <a href="${contextPath}/dashboard" style="text-decoration: none; color: var(--primary-color); font-weight: bold; font-size: 0.9rem; text-transform: uppercase;">
                <i class="fas fa-arrow-left"></i> Torna alla Dashboard
            </a>
            <h1 style="font-size: 2rem; color: var(--primary-dark); margin-top: 10px;">Richiesta #${richiesta.id}</h1>
            <p style="color: #666;">Analizza le informazioni ricevute e decidi come intervenire.</p>
        </div>

        <main class="content">
            <div class="grid-2">
                <!-- COLONNA SINISTRA: DATI RICHIEDENTE -->
                <section class="card">
                    <h2 style="font-size: 1.2rem; margin-top: 0; color: var(--primary-color); border-bottom: 1px solid #eee; padding-bottom: 10px; margin-bottom: 20px;">
                        Informazioni Generali
                    </h2>
                    
                    <div class="info-group">
                        <label>Nome Richiedente</label>
                        <p>${richiesta.nomeRichiedente}</p>
                    </div>

                    <div class="info-group">
                        <label>Contatto Email</label>
                        <p>${richiesta.contattoEmail}</p>
                    </div>

                    <div class="info-group">
                        <label>Luogo dell'Emergenza</label>
                        <p>${richiesta.indirizzo}</p>
                    </div>

                    <div class="grid-2">
                        <div class="info-group">
                            <label>Ricevuta il</label>
                            <p>${richiesta.dataOraRichiesta?string?replace("T", ", ")}</p>
                        </div>
                        <div class="info-group">
                            <label>Priorità</label>
                            <p><span class="badge badge-${richiesta.priorita?lower_case}">${richiesta.priorita}</span></p>
                        </div>
                    </div>

                    <div class="info-group">
                        <label>Tipologia Emergenza</label>
                        <p>
                            <span style="background: #f5f5f5; padding: 5px 12px; border-radius: 4px; border: 1px solid #ddd; color: #555; font-size: 0.9rem; font-weight: bold; text-transform: uppercase;">
                                ${richiesta.tipologia}
                            </span>
                        </p>
                    </div>

                    <#if richiesta.foto??>
                        <div class="info-group" style="margin-top: 25px;">
                            <label>Foto Allegata</label>
                            <div style="margin-top: 10px;">
                                <img src="${contextPath}/${richiesta.foto}" alt="Foto Emergenza" style="max-width: 100%; border-radius: 8px; border: 1px solid #ddd; box-shadow: 0 4px 10px rgba(0,0,0,0.1);">
                            </div>
                        </div>
                    </#if>
                </section>

                <!-- COLONNA DESTRA: DESCRIZIONE E AZIONI -->
                <div class="actions-col">
                    <section class="card" style="margin-bottom: 25px;">
                        <h2 style="font-size: 1.2rem; margin-top: 0; color: var(--primary-color); border-bottom: 1px solid #eee; padding-bottom: 10px; margin-bottom: 20px;">
                            Descrizione Evento
                        </h2>
                        <p style="font-size: 1.1rem; font-weight: 500; line-height: 1.6; background: #f9f9f9; padding: 20px; border-radius: 6px; border-left: 5px solid #ffc107; font-style: italic;">
                            "${richiesta.descrizione}"
                        </p>
                    </section>

                    <section class="card">
                        <h2 style="font-size: 1.2rem; margin-top: 0; color: var(--primary-color); border-bottom: 1px solid #eee; padding-bottom: 10px; margin-bottom: 20px;">
                            Azioni Disponibili
                        </h2>
                        <p style="color: #666; font-size: 0.9rem; margin-bottom: 25px;">
                            Puoi procedere con l'attivazione della missione o rifiutare la segnalazione se non ritenuta valida.
                        </p>

                        <div style="display: flex; flex-direction: column; gap: 15px;">
                            <a href="${contextPath}/nuova-missione?richiestaId=${richiesta.id}" class="btn btn-primary btn-large btn-full">
                                <i class="fas fa-plus-circle"></i> Configura e Crea Missione
                            </a>

                            <form action="${contextPath}/gestione-richiesta" method="POST">
                                <input type="hidden" name="id" value="${richiesta.id}">
                                <input type="hidden" name="azione" value="RIFIUTA">
                                <button type="submit" class="btn btn-outline btn-full" style="color: var(--danger); border-color: var(--danger); background: transparent; border: 2px solid var(--danger);" 
                                        onclick="return confirm('Sei sicuro di voler rifiutare questa richiesta? L\'azione è irreversibile.');">
                                    <i class="fas fa-times-circle"></i> Rifiuta Richiesta
                                </button>
                            </form>
                        </div>
                    </section>
                </div>
            </div>
        </main>

        <footer style="margin-top: 50px; padding-bottom: 30px; text-align: center; color: #aaa; font-size: 0.85rem;">
            <p>&copy; 2026 SoccorsoWeb - Area Gestione Emergenze</p>
        </footer>
    </div>

    <script src="${contextPath}/js/main.js"></script>
</body>
</html>