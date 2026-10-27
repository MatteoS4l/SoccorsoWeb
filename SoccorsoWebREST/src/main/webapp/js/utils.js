const API_BASE = "/SoccorsoWebREST/api";

async function fetchProtetta(url, opzioni = {}) {
    const token = localStorage.getItem("token");
    

    if (!opzioni.headers) opzioni.headers = {};
    

    if (token) {
        opzioni.headers["Authorization"] = `Bearer ${token}`;
    }
    

    if (opzioni.body && !opzioni.headers["Content-Type"]) {
        opzioni.headers["Content-Type"] = "application/json";
    }

    const res = await fetch(url, opzioni);
    

    if (res.status === 401) {
        console.warn("Sessione scaduta o token non valido.");

    }
    
    return res;
}


async function caricaOpzioniEnum(percorso) {
    try {
        const res = await fetch(`${API_BASE}/richieste/${percorso}`);
        if (!res.ok) return [];
        return await res.json();
    } catch (err) {
        console.error("Errore nel caricamento Enum:", err);
        return [];
    }
}


function formattaData(dataIso) {
    if (!dataIso) return "N/D";
    return dataIso.replace('T', ' ').substring(0, 16);
}
