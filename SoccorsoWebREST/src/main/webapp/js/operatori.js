async function showOperatori(soloLiberi = false) {
    if (!authToken) return showLogin();
    const content = document.getElementById("content");
    content.innerHTML = "<h4>Caricamento operatori...</h4>";
    
    try {
        let url = `${API_BASE}/operatori`;
        if (soloLiberi) url += `?stato=libero`;
        
        const res = await fetchProtetta(url);
        const data = await res.json();
        
        let html = `
            <div class="d-flex justify-content-between align-items-center mb-3">
                <h3>Elenco Operatori</h3>
                <div class="form-check form-switch">
                    <input class="form-check-input" type="checkbox" id="filtroLiberi" ${soloLiberi ? 'checked' : ''} onchange="showOperatori(this.checked)">
                    <label class="form-check-label" for="filtroLiberi">Mostra solo liberi</label>
                </div>
            </div>
            <div class="row g-3">`;
        
        data.forEach(o => {
            html += `
                <div class="col-md-4">
                    <div class="card h-100 shadow-sm border">
                        <div class="card-body">
                            <h5 class="card-title">${o.nome} ${o.cognome}</h5>
                            <p class="card-text text-muted small">${o.email}</p>
                            <button class="btn btn-sm btn-outline-primary" onclick="showDettaglioOperatore(${o.id})">Vedi Attività</button>
                        </div>
                    </div>
                </div>`;
        });
        
        html += "</div>";
        content.innerHTML = html;
    } catch (err) { content.innerHTML = "Errore caricamento."; }
}

async function showDettaglioOperatore(id) {
    const content = document.getElementById("content");
    try {
        const resO = await fetchProtetta(`${API_BASE}/operatori/${id}`);
        const o = await resO.json();
        
        const resM = await fetchProtetta(`${API_BASE}/operatori/${id}/missioni`);
        const missioni = await resM.json();
        
        let missioniHtml = missioni.length > 0 
            ? `<div class="list-group list-group-flush mt-2">` + missioni.map(m => `
                <div class="list-group-item">
                    <div class="d-flex justify-content-between align-items-center">
                        <span class="fw-bold">Missione #${m.id}</span>
                        <span class="badge ${m.stato === 'COMPLETATA' ? 'bg-success' : 'bg-warning'}">${m.stato}</span>
                    </div>
                    <div class="small text-muted">${m.obiettivo}</div>
                    <div class="small text-muted"><i class="bi bi-geo-alt"></i> ${m.richiesta.indirizzo}</div>
                </div>`).join('') + `</div>`
            : `<p class="text-muted small mt-2">Nessuna missione registrata.</p>`;

        content.innerHTML = `
            <div class="card border shadow-sm">
                <div class="card-header bg-white d-flex justify-content-between">
                    <span>Scheda Operatore: ${o.nome} ${o.cognome}</span>
                    <button class="btn-close" onclick="showOperatori()"></button>
                </div>
                <div class="card-body">
                    <p class="mb-1"><strong>Email:</strong> ${o.email}</p>
                    <p class="mb-1"><strong>Ruolo:</strong> ${o.ruolo}</p>
                    
                    <div class="row mt-3 mb-3">
                        <div class="col-md-6">
                            <h6><i class="bi bi-tools"></i> Skills:</h6>
                            <div class="d-flex flex-wrap gap-1">
                                ${o.skills && o.skills.length > 0 ? o.skills.map(s => `<span class="badge bg-light text-dark border">${s}</span>`).join('') : '<span class="text-muted small">Nessuna</span>'}
                            </div>
                        </div>
                        <div class="col-md-6">
                            <h6><i class="bi bi-card-checklist"></i> Patenti:</h6>
                            <div class="d-flex flex-wrap gap-1">
                                ${o.patenti && o.patenti.length > 0 ? o.patenti.map(p => `<span class="badge bg-info-subtle text-info-emphasis border border-info">${p}</span>`).join('') : '<span class="text-muted small">Nessuna</span>'}
                            </div>
                        </div>
                    </div>

                    <h6 class="border-top pt-3">Missioni Partecipate:</h6>
                    ${missioniHtml}
                    <div class="mt-4">
                        <button class="btn btn-secondary btn-sm" onclick="showOperatori()">Indietro alla lista</button>
                    </div>
                </div>
            </div>
        `;
    } catch (err) { alert("Errore caricamento attività."); }
}
