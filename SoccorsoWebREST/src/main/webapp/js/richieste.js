

async function showHome() {
    const content = document.getElementById("content");
    content.innerHTML = "<h4>Caricamento form...</h4>";
    const tipologie = await caricaOpzioniEnum("tipologie");
    const priorita = await caricaOpzioniEnum("priorita");
    
    let tipologiaHtml = `<select id="r_tipologia" class="form-select">`;
    tipologie.forEach(t => { tipologiaHtml += `<option value="${t}">${t.replace(/_/g, ' ')}</option>`; });
    tipologiaHtml += `</select>`;

    let prioritaHtml = `<select id="r_priorita" class="form-select">`;
    priorita.forEach(p => { 
        prioritaHtml += `<option value="${p}" ${p === 'MEDIA' ? 'selected' : ''}>${p}</option>`; 
    });
    prioritaHtml += `</select>`;

    content.innerHTML = `
        <div class="row justify-content-center">
            <div class="col-md-6">
                <div class="card card-body border shadow-sm">
                    <h3 class="mb-4">Nuova Richiesta di Soccorso</h3>
                    <div class="mb-3"><label class="form-label">Nome e Cognome</label><input type="text" id="r_nome" class="form-control"></div>
                    <div class="mb-3"><label class="form-label">Email di contatto</label><input type="email" id="r_email" class="form-control"></div>
                    <div class="mb-3"><label class="form-label">Indirizzo</label><input type="text" id="r_indirizzo" class="form-control"></div>
                    <div class="mb-3"><label class="form-label">Tipologia Emergenza</label>${tipologiaHtml}</div>
                    <div class="mb-3"><label class="form-label">Priorità</label>${prioritaHtml}</div>
                    <div class="mb-3"><label class="form-label">Descrizione</label><textarea id="r_descrizione" class="form-control" rows="3"></textarea></div>
                    <button class="btn btn-primary w-100" onclick="inviaRichiesta()">Invia Richiesta</button>
                </div>
            </div>
        </div>
    `;
}

async function inviaRichiesta() {
    const data = {
        nomeRichiedente: document.getElementById("r_nome").value,
        contattoEmail: document.getElementById("r_email").value,
        indirizzo: document.getElementById("r_indirizzo").value,
        tipologia: document.getElementById("r_tipologia").value,
        priorita: document.getElementById("r_priorita").value,
        descrizione: document.getElementById("r_descrizione").value
    };

    try {
        const res = await fetch(`${API_BASE}/richieste`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(data)
        });
        const r = await res.json();
        if (res.ok) {
            document.getElementById("content").innerHTML = `
                <div class="alert alert-success mt-5">
                    <h4>Richiesta Ricevuta!</h4>
                    <p>Simulazione email: <button class="btn btn-success" onclick="showConvalida('${r.codiceConvalida}')">Convalida ora</button></p>
                </div>`;
        }
    } catch (err) { alert("Errore invio"); }
}

async function showConvalida(token) {
    try {
        const res = await fetch(`${API_BASE}/richieste/convalida/${token}`);
        if (res.ok) {
            document.getElementById("content").innerHTML = `
                <div class="text-center mt-5"><h2 class="text-success">Convalida Riuscita!</h2><button class="btn btn-primary" onclick="showHome()">Home</button></div>`;
        }
    } catch (err) { alert("Errore convalida"); }
}



let currentPage = 1;
const PAGE_SIZE = 10;

async function showRichieste(statoFiltro = '', page = 1) {
    if (!authToken) return showLogin();
    currentPage = page;
    const content = document.getElementById("content");
    const stati = await caricaOpzioniEnum("stati");
    
    let filterHtml = `<select id="filtroStato" class="form-select form-select-sm" style="width: 200px;"><option value="">Tutti gli stati</option>`;
    stati.forEach(s => { filterHtml += `<option value="${s}" ${statoFiltro === s ? 'selected' : ''}>${s.replace(/_/g, ' ')}</option>`; });
    filterHtml += `</select>`;

    try {
        let url = `${API_BASE}/richieste?page=${currentPage}&pageSize=${PAGE_SIZE}`;
        if (statoFiltro) url += `&stato=${statoFiltro}`;
        
        const res = await fetchProtetta(url);
        const data = await res.json();
        
        let html = `
            <div class="d-flex justify-content-between align-items-center mb-3">
                <h3>Gestione Richieste</h3>
                <div class="d-flex gap-2">${filterHtml}<button class="btn btn-sm btn-dark" onclick="showRichieste(document.getElementById('filtroStato').value, 1)">Filtra</button></div>
            </div>
            <table class="table table-hover border">
                <thead class="table-light"><tr><th>ID</th><th>Data</th><th>Tipologia</th><th>Priorità</th><th>Stato</th><th>Azioni</th></tr></thead><tbody>`;
        
        data.forEach(r => {
            html += `<tr><td>${r.id}</td><td>${formattaData(r.dataOraRichiesta)}</td><td>${r.tipologia}</td><td>${r.priorita}</td><td><span class="badge bg-secondary">${r.stato}</span></td>
                <td><button class="btn btn-sm btn-outline-primary" onclick="showDettaglioRichiesta(${r.id})">Dettaglio</button></td></tr>`;
        });
        
        if (data.length === 0 && currentPage === 1) {
            html += `<tr><td colspan="6" class="text-center">Nessuna richiesta trovata.</td></tr>`;
        }
        
        html += "</tbody></table>";

        html += `
            <div class="d-flex justify-content-center gap-2 mt-3">
                <button class="btn btn-sm btn-outline-secondary" ${currentPage === 1 ? 'disabled' : ''} 
                        onclick="showRichieste('${statoFiltro}', ${currentPage - 1})">Precedente</button>
                <span class="align-self-center px-3 fw-bold">Pagina ${currentPage}</span>
                <button class="btn btn-sm btn-outline-secondary" ${data.length < PAGE_SIZE ? 'disabled' : ''} 
                        onclick="showRichieste('${statoFiltro}', ${currentPage + 1})">Successiva</button>
            </div>
        `;

        content.innerHTML = html;
    } catch (err) { content.innerHTML = "Errore."; }
}

async function showDettaglioRichiesta(id) {
    try {
        const res = await fetchProtetta(`${API_BASE}/richieste/${id}`);
        const r = await res.json();
        
        let actionsHtml = `<button class="btn btn-secondary btn-sm" onclick="showRichieste()">Indietro</button> `;
        
        switch (r.stato) {
            case 'DA_CONVALIDARE':
            case 'IN_ATTESA':
                actionsHtml += `<button class="btn btn-danger btn-sm" onclick="rifiutaRichiesta(${r.id})">Rifiuta</button> `;
                if (currentUser.ruolo === 'ADMIN' && r.stato === 'IN_ATTESA') {
                    actionsHtml += `<button class="btn btn-primary btn-sm" onclick="showNuovaMissione(${r.id})">Avvia Missione</button> `;
                }
                break;
            case 'CONFERMATA':
                actionsHtml += `<span class="text-muted ms-2 small">Missione in corso...</span>`;
                break;
            case 'CHIUSA':
                actionsHtml += `<span class="text-success ms-2 small">Richiesta Chiusa</span>`;
                break;
            case 'RIFIUTATA':
                actionsHtml += `<span class="text-danger ms-2 small">Richiesta Rifiutata</span>`;
                break;
        }

        document.getElementById("content").innerHTML = `
            <div class="card border"><div class="card-header bg-white">Dettaglio Richiesta #${r.id}</div>
                <div class="card-body">
                    <p><strong>Richiedente:</strong> ${r.nomeRichiedente}</p>
                    <p><strong>Priorità:</strong> ${r.priorita}</p>
                    <p><strong>Stato:</strong> <span class="badge bg-info">${r.stato}</span></p>
                    <p><strong>Descrizione:</strong><br>${r.descrizione || '-'}</p>
                    <hr>
                    ${actionsHtml}
                </div></div>`;
    } catch (err) { alert("Errore"); }
}

async function rifiutaRichiesta(id) {
    if (!confirm("Sicuro?")) return;
    try {
        const res = await fetchProtetta(`${API_BASE}/richieste/${id}/rifiuta`, { method: 'POST' });
        if (res.ok) showRichieste();
    } catch (err) { alert("Errore"); }
}

async function showInsuccessi() {
    try {
        const res = await fetchProtetta(`${API_BASE}/richieste/insuccessi`);
        const data = await res.json();
        let html = `<h3>Insuccessi (< 5 stelle)</h3><table class="table table-bordered mt-3"><thead class="table-danger"><tr><th>ID</th><th>Richiedente</th><th>Tipologia</th></tr></thead><tbody>`;
        data.forEach(r => { html += `<tr><td>${r.id}</td><td>${r.nomeRichiedente}</td><td>${r.tipologia}</td></tr>`; });
        html += "</tbody></table>";
        document.getElementById("content").innerHTML = html;
    } catch (err) { }
}
