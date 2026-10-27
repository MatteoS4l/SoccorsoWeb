<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Chiusura Missione - SoccorsoWeb</title>
    <link rel="stylesheet" href="${contextPath}/css/style.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
</head>
<body class="dashboard-body">

    <#assign activePage = 'missioni'>
    <#include "includes/header.ftl">

    <div class="container">
        
        <div class="welcome-section" style="margin-bottom: 40px; margin-top: 20px;">
            <a href="${contextPath}/dettaglio-missione?id=${missione.id}" style="text-decoration: none; color: var(--primary-color); font-weight: bold; font-size: 0.9rem; text-transform: uppercase;">
                <i class="fas fa-arrow-left"></i> Annulla e torna ai dettagli
            </a>
            <h1 style="font-size: 2rem; color: var(--primary-dark); margin-top: 15px;">Chiusura Missione #${missione.id}</h1>
            <p style="color: #666;">Procedura finale di inventario e ripristino operatività mezzi.</p>
        </div>

        <main class="content">
            <form action="${contextPath}/rientro-missione" method="POST">
                <input type="hidden" name="id" value="${missione.id}">

                <div class="grid-2">
                    <!-- SEZIONE MATERIALI -->
                    <section class="card" style="padding: 0; overflow: hidden;">
                        <div style="padding: 20px 25px; border-bottom: 1px solid #eee; background: #fafafa;">
                            <h2 style="margin: 0; font-size: 1.2rem;">1. Inventario Materiali</h2>
                        </div>
                        
                        <div style="padding: 25px;">
                            <p style="font-size: 0.9rem; color: #666; margin-bottom: 20px;">
                                Indica le quantità integre o inutilizzate che rientrano effettivamente in magazzino.
                            </p>
                            
                            <#if missione.materiali?has_content>
                                <div class="checkbox-list" style="height: auto; min-height: 250px; background: white; padding: 0;">
                                    <table style="width: 100%; border-collapse: collapse; font-size: 0.9rem;">
                                        <thead style="position: sticky; top: 0; background: #f8f9fa; z-index: 10;">
                                            <tr>
                                                <th style="padding: 12px; text-align: left; border-bottom: 1px solid #ddd;">Materiale</th>
                                                <th style="padding: 12px; text-align: right; border-bottom: 1px solid #ddd;">Q.tà Rientro</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <#list missione.materiali as mat>
                                                <tr style="border-bottom: 1px solid #eee;">
                                                    <td style="padding: 12px;">
                                                        <strong>${mat.tipo}</strong>
                                                        <#if mat.descrizione??><br><small style="color: #888;">${mat.descrizione}</small></#if>
                                                    </td>
                                                    <td style="padding: 12px; text-align: right;">
                                                        <input type="number" name="restituisci_mat_${mat.id}" 
                                                               value="0" min="0" 
                                                               style="width: 75px; padding: 8px; border: 1px solid #ddd; border-radius: 4px; text-align: center;">
                                                    </td>
                                                </tr>
                                            </#list>
                                        </tbody>
                                    </table>
                                </div>
                            <#else>
                                <div class="alert alert-info">
                                    Nessun materiale era stato assegnato a questa missione.
                                </div>
                            </#if>
                        </div>
                    </section>

                    <!-- SEZIONE MEZZI -->
                    <section class="card" style="padding: 0; overflow: hidden;">
                        <div style="padding: 20px 25px; border-bottom: 1px solid #eee; background: #fafafa;">
                            <h2 style="margin: 0; font-size: 1.2rem;">2. Ripristino Mezzi</h2>
                        </div>
                        
                        <div style="padding: 25px;">
                            <p style="font-size: 0.9rem; color: #666; margin-bottom: 20px;">
                                I seguenti mezzi verranno contrassegnati come <strong>disponibili</strong> per nuove missioni.
                            </p>
                            
                            <div class="checkbox-list" style="height: auto; min-height: 150px; background: #fcfcfc; margin-bottom: 30px;">
                                <#list missione.mezzi as m>
                                    <div style="padding: 15px; border-bottom: 1px solid #eee; display: flex; align-items: center; gap: 15px;">
                                        <i class="fas fa-check-circle" style="color: var(--success); font-size: 1.2rem;"></i>
                                        <div>
                                            <strong style="display: block; color: var(--text-dark);">${m.tipo}</strong>
                                            <small style="color: #888;">${m.descrizione!"Identificativo non presente"}</small>
                                        </div>
                                    </div>
                                </#list>
                            </div>

                            <div style="border-top: 1px solid #eee; padding-top: 30px;">
                                <h3 style="font-size: 1.1rem; margin-bottom: 15px;">3. Valutazione Esito Missione</h3>
                                <p style="font-size: 0.85rem; color: #666; margin-bottom: 15px;">
                                    Assegna un punteggio da 1 a 5 per indicare il livello di successo dell'intervento.
                                </p>
                                <div class="star-rating">
                                    <input type="radio" id="star5" name="valutazione" value="5" required />
                                    <label for="star5" title="5 stelle"><i class="fas fa-star"></i></label>
                                    <input type="radio" id="star4" name="valutazione" value="4" />
                                    <label for="star4" title="4 stelle"><i class="fas fa-star"></i></label>
                                    <input type="radio" id="star3" name="valutazione" value="3" />
                                    <label for="star3" title="3 stelle"><i class="fas fa-star"></i></label>
                                    <input type="radio" id="star2" name="valutazione" value="2" />
                                    <label for="star2" title="2 stelle"><i class="fas fa-star"></i></label>
                                    <input type="radio" id="star1" name="valutazione" value="1" />
                                    <label for="star1" title="1 stella"><i class="fas fa-star"></i></label>
                                </div>
                            </div>

                            <div style="margin-top: 40px;">
                                <button type="submit" class="btn btn-danger btn-full" style="padding: 18px; font-size: 1.1rem;">
                                    Conferma Chiusura Totale
                                </button>
                            </div>
                        </div>
                    </section>
                </div>
            </form>
        </main>

        <footer style="margin-top: 60px; padding-bottom: 40px; text-align: center; color: #bbb; font-size: 0.85rem;">
            <p>&copy; 2026 SoccorsoWeb - Logistica di Rientro</p>
        </footer>
    </div>

    <script src="${contextPath}/js/main.js"></script>
</body>
</html>
