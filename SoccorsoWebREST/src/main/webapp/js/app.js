
document.addEventListener("DOMContentLoaded", () => {
    updateMenu();
    showHome(); 
    
    // Logo link
    document.getElementById("home-link").onclick = (e) => {
        e.preventDefault();
        showHome();
    };
});

//aggiorno il menun in base a login 

function updateMenu() {
    const menu = document.getElementById("nav-menu");
    if (!authToken || !currentUser) {
        menu.innerHTML = `<li class="nav-item"><a class="nav-link" onclick="showLogin()">Login Operatore</a></li>`;
    } else {
        const isAdmin = currentUser.ruolo === 'ADMIN';
        
        let menuHtml = '';
        if (isAdmin) {
            menuHtml += `<li class="nav-item"><a class="nav-link" onclick="showRichieste()">Gestione Richieste</a></li>`;
        }
        
        menuHtml += `<li class="nav-item"><a class="nav-link" onclick="showMissioni()">Missioni</a></li>`;
        
        if (isAdmin) {
            menuHtml += `<li class="nav-item"><a class="nav-link" onclick="showOperatori()">Operatori</a></li>`;
        }
        
        menuHtml += `<li class="nav-item"><a class="nav-link text-danger ms-3" onclick="logout()">Logout</a></li>`;
        
        menu.innerHTML = menuHtml;
    }
}
