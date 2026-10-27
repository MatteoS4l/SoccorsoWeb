<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Attivazione Missione - SoccorsoWeb</title>
    <link rel="stylesheet" href="${contextPath}/css/style.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
</head>
<body class="dashboard-body">

    <#assign activePage = 'dashboard'>
    <#include "includes/header.ftl">

    <div class="container">
        
        <div class="welcome-section" style="margin-bottom: 40px; margin-top: 20px;">
            <a href="${contextPath}/dashboard" style="text-decoration: none; color: var(--primary-color); font-weight: bold; font-size: 0.9rem; text-transform: uppercase;">
                <i class="fas fa-arrow-left"></i> Annulla e torna alla Dashboard
            </a>
            <h1 style="font-size: 2rem; color: var(--primary-dark); margin-top: 15px;">Configurazione Missione Operativa</h1>
            <p style="color: #666;">Stai attivando un intervento per la segnalazione <strong>#${richiesta.id}</strong>.</p>
        </div>

        <form action="${contextPath}/nuova-missione" method="POST">
            <input type="hidden" name="richiestaId" value="${richiesta.id}">

            <div class="grid-2">
                <!-- COLONNA SINISTRA: DATI E OBIETTIVO -->
                <div class="left-col">
                    <section class="card" style="margin-bottom: 25px;">
                        <h2 style="font-size: 1.2rem; margin-top: 0; border-bottom: 1px solid #eee; padding-bottom: 10px; margin-bottom: 20px;">
                            Dettagli Emergenza
                        </h2>
                        
                        <div style="background: #f8f9fa; padding: 20px; border-radius: 8px; border-left: 4px solid #1976d2; margin-bottom: 25px;">
                            <div class="grid-2">
                                <div class="info-group">
                                    <label>Evento</label>
                                    <p><strong>${richiesta.tipologia}</strong></p>
                                </div>
                                <div class="info-group">
                                    <label>Priorità</label>
                                    <p><span class="badge badge-${richiesta.priorita?lower_case}">${richiesta.priorita}</span></p>
                                </div>
                            </div>
                            <div class="info-group" style="margin-top: 15px;">
                                <label>Località</label>
                                <p>${richiesta.indirizzo}</p>
                            </div>
                            <div class="info-group" style="margin-top: 15px;">
                                <label>Descrizione Segnalata</label>
                                <p style="font-style: italic; font-size: 0.95rem; color: #555;">"${richiesta.descrizione}"</p>
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="obiettivo">Obiettivo Formale della Missione</label>
                            <input type="text" id="obiettivo" name="obiettivo" required 
                                   value="Soccorso ${richiesta.tipologia} @ ${richiesta.indirizzo}">
                        </div>

                        <div class="form-group">
                            <label for="caposquadraId">Responsabile Unità (Caposquadra)</label>
                            <select id="caposquadraId" name="caposquadraId" required>
                                <option value="">-- Seleziona Responsabile --</option>
                                <#if operatoriDisponibili??>
                                    <#list operatoriDisponibili as op>
                                        <option value="${op.id}">${op.nome} ${op.cognome}</option>
                                    </#list>
                                </#if>
                            </select>
                        </div>

                        <div class="form-group">
                            <label for="note">Istruzioni Tattiche per la Squadra</label>
                            <textarea id="note" name="note" rows="3" placeholder="Inserisci eventuali note o pericoli specifici..."></textarea>
                        </div>
                    </section>
                </div>

                <!-- COLONNA DESTRA: ASSEGNAZIONE RISORSE -->
                <div class="right-col">
                    <section class="card" style="margin-bottom: 20px;">
                        <h2 style="font-size: 1.2rem; margin-top: 0; border-bottom: 1px solid #eee; padding-bottom: 10px; margin-bottom: 20px;">
                            Unità e Mezzi
                        </h2>
                        
                        <div class="form-group">
                            <label>Squadra Operativa (Scegli componenti)</label>
                            <div class="checkbox-list" style="height: 180px; background: #fafafa; border: 1px solid #eee; overflow-y: auto;">
                                <#if operatoriDisponibili?? && operatoriDisponibili?has_content>
                                    <#list operatoriDisponibili as op>
                                        <div class="checkbox-item" style="display: flex; align-items: center; padding: 10px; border-bottom: 1px solid #eee; gap: 15px;">
                                            <input type="checkbox" id="op_${op.id}" name="operatoriIds" value="${op.id}" style="width: 18px; height: 18px;">
                                            <label for="op_${op.id}" style="font-weight: normal; cursor: pointer; flex-grow: 1;">
                                                <strong>${op.nome} ${op.cognome}</strong>
                                                <#if op.skills?has_content>
                                                    <small style="display:block; color: #888;">
                                                        Skills: <#list op.skills as skill>${skill.descrizione}<#if skill?has_next>, </#if></#list>
                                                    </small>
                                                </#if>
                                            </label>
                                        </div>
                                    </#list>
                                <#else>
                                    <p style="padding: 20px; color: var(--danger); text-align: center;">Nessun operatore disponibile.</p>
                                </#if>
                            </div>
                        </div>

                        <div class="form-group">
                            <label>Mezzi di Trasporto</label>
                            <div class="checkbox-list" style="height: 130px; background: #fafafa; border: 1px solid #eee; overflow-y: auto;">
                                <#if mezziDisponibili?? && mezziDisponibili?has_content>
                                    <#list mezziDisponibili as mezzo>
                                        <div class="checkbox-item" style="display: flex; align-items: center; padding: 10px; border-bottom: 1px solid #eee; gap: 15px;">
                                            <input type="checkbox" id="mezzo_${mezzo.id}" name="mezziIds" value="${mezzo.id}" style="width: 18px; height: 18px;">
                                            <label for="mezzo_${mezzo.id}" style="font-weight: normal; cursor: pointer; flex-grow: 1;">
                                                <strong>${mezzo.tipo}</strong> <small>(${mezzo.descrizione!"Nessuna targa"})</small>
                                            </label>
                                        </div>
                                    </#list>
                                <#else>
                                    <p style="padding: 20px; color: var(--danger); text-align: center;">Nessun mezzo disponibile.</p>
                                </#if>
                            </div>
                        </div>

                        <div class="form-group">
                            <label>Materiali e Attrezzature (Magazzino)</label>
                            <div class="checkbox-list" style="height: 200px; background: #fafafa; border: 1px solid #eee; overflow-y: auto; padding: 0;">
                                <#if materiali?? && materiali?has_content>
                                    <table style="width: 100%; border-collapse: collapse; font-size: 0.9rem;">
                                        <thead style="position: sticky; top: 0; background: #eee; z-index: 10;">
                                            <tr>
                                                <th style="padding: 10px; text-align: left; border-bottom: 1px solid #ddd;">Tipo</th>
                                                <th style="padding: 10px; text-align: center; border-bottom: 1px solid #ddd;">Disp.</th>
                                                <th style="padding: 10px; text-align: right; border-bottom: 1px solid #ddd;">Prelievo</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <#list materiali as mat>
                                                <tr style="border-bottom: 1px solid #eee;">
                                                    <td style="padding: 10px;"><strong>${mat.tipo}</strong></td>
                                                    <td style="padding: 10px; text-align: center; color: #666;">${mat.quantitaDisponibile}</td>
                                                    <td style="padding: 10px; text-align: right;">
                                                        <input type="number" name="quantita_mat_${mat.id}" 
                                                               value="0" min="0" max="${mat.quantitaDisponibile}"
                                                               style="width: 60px; padding: 5px; border: 1px solid #ddd; border-radius: 4px; text-align: center;">
                                                    </td>
                                                </tr>
                                            </#list>
                                        </tbody>
                                    </table>
                                <#else>
                                    <p style="padding: 20px; color: #aaa; text-align: center;">Magazzino vuoto.</p>
                                </#if>
                            </div>
                        </div>
                    </section>

                    <div style="margin-top: 30px;">
                        <button type="submit" class="btn btn-primary btn-full" style="padding: 18px; font-size: 1.1rem;">
                            Attiva Squadra e Inizia Missione
                        </button>
                    </div>
                </div>
            </div>
        </form>

        <footer style="margin-top: 60px; padding-bottom: 40px; text-align: center; color: #bbb; font-size: 0.85rem;">
            <p>&copy; 2026 SoccorsoWeb - Area Comando Centrale</p>
        </footer>
    </div>

        <script src="${contextPath}/js/main.js"></script>

        <script src="${contextPath}/js/controlloCapoSq.js"></script>

    </body>

    </html>

    