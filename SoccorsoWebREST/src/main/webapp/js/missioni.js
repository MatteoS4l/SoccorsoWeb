
async function showMissioni(soloInsuccessi = false) {
    if (!authToken) return showLogin();
    const content = document.getElementById("content");
    content.innerHTML = "<h4>Caricamento missioni...</h4>";

    try {
        const res = await fetchProtetta(`${API_BASE}/missioni`);
        let data = await res.json();

        if (soloInsuccessi) {
            data = data.filter(m => m.livelloSuccesso !== null && m.livelloSuccesso < 5);
        }

        let html = `
            <div class="d-flex justify-content-between align-items-center mb-3">
                <h3>Elenco Missioni</h3>
                <div class="form-check form-switch">
                    <input class="form-check-input" type="checkbox" id="filtroMInsuccessi" ${soloInsuccessi ? 'checked' : ''} 
                           onchange="showMissioni(this.checked)">
                    <label class="form-check-label" for="filtroMInsuccessi">Mostra solo con insuccesso (< 5 stelle)</label>
                </div>
            </div>
            <table class="table table-hover border" style="table-layout: fixed;">
                <thead class="table-light">
                    <tr>
                        <th style="width: 80px;">ID</th>
                        <th>Obiettivo</th>
                        <th style="width: 120px;">Stato</th>
                        <th style="width: 150px;">Valutazione</th>
                        <th style="width: 100px;">Azioni</th>
                    </tr>
                </thead>
                <tbody>`;

        if (data.length === 0) {
            html += `<tr><td colspan="5" class="text-center text-muted p-4">Nessuna missione trovata con i criteri selezionati.</td></tr>`;
        } else {
            data.forEach(m => {
                let rating = m.livelloSuccesso ? "★".repeat(m.livelloSuccesso) + "☆".repeat(5 - m.livelloSuccesso) : "N/D";
                html += `<tr>
                    <td>${m.id}</td>
                    <td class="text-truncate">${m.obiettivo}</td>
                    <td><span class="badge ${m.stato === 'ATTIVA' ? 'bg-primary' : 'bg-success'}">${m.stato}</span></td>
                    <td class="text-warning fw-bold" style="font-family: monospace;">${rating}</td>
                    <td><button class="btn btn-sm btn-outline-dark" onclick="showDettaglioMissione(${m.id})">Vedi</button></td>
                </tr>`;
            });
        }
        html += "</tbody></table>";
        content.innerHTML = html;
    } catch (err) { content.innerHTML = "Errore caricamento missioni."; }
}

async function showDettaglioMissione(id) {
    const content = document.getElementById("content");
    try {
        const res = await fetchProtetta(`${API_BASE}/missioni/${id}`);
        const m = await res.json();

        let opsHtml = m.operatori.map(o => `<li>${o.nome} ${o.cognome}</li>`).join('');
        
        content.innerHTML = `
            <div class="card border shadow-sm">
                <div class="card-header bg-white fw-bold">Missione #${m.id} - ${m.stato}</div>
                <div class="card-body">
                    <h5>Obiettivo: ${m.obiettivo}</h5>
                    <p class="text-muted">Iniziata il: ${formattaData(m.dataOraInizio)}</p>
                    <hr>
                    <p><strong>Caposquadra:</strong> ${m.caposquadra ? m.caposquadra.nome + ' ' + m.caposquadra.cognome : 'N/D'}</p>
                    <p><strong>Squadra:</strong></p>
                    <ul>${opsHtml}</ul>
                    <hr>
                    ${m.stato === 'ATTIVA' ? `<button class="btn btn-warning" onclick="showChiusuraMissione(${m.id})">Concludi e Valuta</button>` : `<p><strong>Valutazione:</strong> ${m.livelloSuccesso}/5 stelle</p>`}
                    <button class="btn btn-secondary" onclick="showMissioni()">Indietro</button>
                </div>
            </div>`;
    } catch (err) { alert("Errore."); }
}

function showChiusuraMissione(id) {
    document.getElementById("content").innerHTML = `
        <div class="card border shadow-sm">
            <div class="card-header bg-dark text-white">Chiusura Missione #${id}</div>
            <div class="card-body">
                <h5>Valutazione Intervento</h5>
                <div class="mb-3">
                    <label class="form-label">Livello di Successo (1-5 stelle)</label>
                    <select id="m_rating" class="form-select">
                        <option value="5">5 Stelle (Successo Totale)</option>
                        <option value="4">4 Stelle</option>
                        <option value="3">3 Stelle</option>
                        <option value="2">2 Stelle</option>
                        <option value="1">1 Stella (Fallimento)</option>
                    </select>
                </div>
                <div class="mb-3">
                    <label class="form-label">Note Finali</label>
                    <textarea id="m_note" class="form-control" rows="3"></textarea>
                </div>
                <button class="btn btn-success" onclick="eseguiChiusuraMissione(${id})">Conferma Chiusura</button>
                <button class="btn btn-light" onclick="showDettaglioMissione(${id})">Annulla</button>
            </div>
        </div>`;
}

async function eseguiChiusuraMissione(id) {
    const rating = document.getElementById("m_rating").value;
    const note = document.getElementById("m_note").value;

    try {
        const res = await fetchProtetta(`${API_BASE}/missioni/${id}/chiusura`, {
            method: 'POST',
            body: JSON.stringify({ livelloSuccesso: parseInt(rating), note: note })
        });

        if (res.ok) {
            alert("Missione conclusa con successo!");
            showMissioni();
        } else { alert("Errore nella chiusura."); }
    } catch (err) { alert("Errore."); }
}

async function showNuovaMissione(richiestaId) {
    const content = document.getElementById("content");
    content.innerHTML = "<h4>Preparazione missione...</h4>";

    try {
        const resR = await fetchProtetta(`${API_BASE}/richieste/${richiestaId}`);
        const richiesta = await resR.json();

        const resO = await fetchProtetta(`${API_BASE}/operatori?stato=libero`);
        const liberi = await resO.json();

        if (liberi.length === 0) {
            content.innerHTML = `<div class="alert alert-warning"><h5>Nessun operatore disponibile!</h5><button class="btn btn-secondary" onclick="showDettaglioRichiesta(${richiestaId})">Indietro</button></div>`;
            return;
        }

        let opOptions = liberi.map(o => `<option value="${o.id}">${o.nome} ${o.cognome}</option>`).join('');
        
        let opCheckboxes = liberi.map(o => `
            <div class="form-check">
                <input class="form-check-input op-checkbox" type="checkbox" value="${o.id}" id="op_${o.id}">
                <label class="form-check-label" for="op_${o.id}">
                    ${o.nome} ${o.cognome}
                </label>
            </div>
        `).join('');

        content.innerHTML = `
            <div class="card border shadow-sm">
                <div class="card-header bg-white fw-bold">Avvia Missione - Richiesta #${richiestaId}</div>
                <div class="card-body">
                    <p class="text-muted small"><strong>Evento:</strong> ${richiesta.tipologia} a ${richiesta.indirizzo}</p>
                    <hr>
                    <div class="mb-3"><label class="form-label">Obiettivo</label><input type="text" id="m_obiettivo" class="form-control"></div>
                    <div class="mb-3"><label class="form-label">Caposquadra</label><select id="m_caposquadra" class="form-select">${opOptions}</select></div>
                    <div class="mb-3">
                        <label class="form-label d-block">Squadra (Seleziona Operatori)</label>
                        <div class="border rounded p-3" style="max-height: 200px; overflow-y: auto;">
                            ${opCheckboxes}
                        </div>
                    </div>
                    <div class="mt-4">
                        <button class="btn btn-primary" onclick="eseguiCreaMissione(${richiestaId})">Crea Missione</button>
                        <button class="btn btn-light border" onclick="showDettaglioRichiesta(${richiestaId})">Annulla</button>
                    </div>
                </div>
            </div>
        `;
    } catch (err) { content.innerHTML = "Errore."; }
}

async function eseguiCreaMissione(richiestaId) {
    const obiettivo = document.getElementById("m_obiettivo").value;
    const caposquadraId = document.getElementById("m_caposquadra").value;
    
    const operatoriIds = Array.from(document.querySelectorAll(".op-checkbox:checked")).map(cb => parseInt(cb.value));

    if (!obiettivo || !caposquadraId || operatoriIds.length === 0) {
        alert("Compila tutto. Seleziona almeno un operatore per la squadra.");
        return;
    }

    const payload = {
        richiestaId: richiestaId,
        adminId: currentUser.id, 
        obiettivo: obiettivo,
        caposquadraId: parseInt(caposquadraId),
        operatoriIds: operatoriIds
    };

    try {
        const res = await fetchProtetta(`${API_BASE}/missioni`, {
            method: 'POST',
            body: JSON.stringify(payload)
        });

        if (res.ok) {
            alert("Missione avviata!");
            showRichieste();
        } else { alert("Errore."); }
    } catch (err) { alert("Errore."); }
}
