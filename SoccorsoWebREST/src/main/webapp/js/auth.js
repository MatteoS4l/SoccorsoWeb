let authToken = localStorage.getItem("token");
let currentUser = JSON.parse(localStorage.getItem("user"));

async function doLogin() {
    const email = document.getElementById("email").value;
    const password = document.getElementById("password").value;
    
    try {
        const res = await fetch(`${API_BASE}/auth/login`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ email, password })
        });
        
        const data = await res.json();
        if (data.token) {
            authToken = data.token;
            currentUser = data.utente; 
            localStorage.setItem("token", data.token);
            localStorage.setItem("user", JSON.stringify(data.utente));
            
            updateMenu();
            
            // in base al ruolo mi reindirzza
            if (currentUser.ruolo === 'ADMIN') {
                showRichieste();
            } else {
                showMissioni();
            }
        } else {
            alert("Credenziali errate");
        }
    } catch (err) {
        alert("Errore di connessione");
    }
}

async function logout() {
    try {

        await fetchProtetta(`${API_BASE}/auth/logout`, { method: 'POST' });
    } catch (err) {
        console.warn("Server logout failed, clearing local data anyway.");
    }
    
    authToken = null;
    currentUser = null;
    localStorage.removeItem("token");
    localStorage.removeItem("user");
    updateMenu();
    showHome();
}

function showLogin() {
    document.getElementById("content").innerHTML = `
        <div class="row justify-content-center">
            <div class="col-md-4">
                <div class="card card-body border shadow-sm">
                    <h3>Login Operatore</h3>
                    <div class="mb-3"><label>Email</label><input type="email" id="email" class="form-control"></div>
                    <div class="mb-3"><label>Password</label><input type="password" id="password" class="form-control"></div>
                    <button class="btn btn-dark w-100" onclick="doLogin()">Entra</button>
                </div>
            </div>
        </div>
    `;
}
