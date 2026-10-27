
function openModal(modalId) {
    const modal = document.getElementById(modalId);
    if (modal) {
        modal.style.display = "block";
        document.body.style.overflow = "hidden"; 
    }
}

function closeModal(modalId) {
    const modal = document.getElementById(modalId);
    if (modal) {
        modal.style.display = "none";
        document.body.style.overflow = "auto"; 
    }
}

// Funzione per la conferma eliminazione
function confermaEliminazione(type, id) {
    if (confirm("Sei sicuro di voler eliminare questa risorsa? L'azione potrebbe fallire se la risorsa è legata a missioni esistenti.")) {

        const form = document.createElement('form');
        form.method = 'POST';
        form.action = 'gestione-risorse';

        const actionInput = document.createElement('input');
        actionInput.type = 'hidden';
        actionInput.name = 'action';
  
        let actionName = "";
        if(type === 'operatore') actionName = "deleteOperatore";
        else if(type === 'mezzo') actionName = "deleteMezzo";
        else if(type === 'materiale') actionName = "deleteMateriale";
        else if(type === 'patente') actionName = "deletePatente";
        else if(type === 'skill') actionName = "deleteSkill";
        
        actionInput.value = actionName;

        const idInput = document.createElement('input');
        idInput.type = 'hidden';
        idInput.name = 'id';
        idInput.value = id;

        form.appendChild(actionInput);
        form.appendChild(idInput);
        document.body.appendChild(form);
        form.submit();
    }
}


window.onclick = function(event) {
    if (event.target.classList.contains('modal')) {
        event.target.style.display = "none";
        document.body.style.overflow = "auto";
    }
};


document.addEventListener('DOMContentLoaded', function() {
    

    const forms = document.querySelectorAll('form');
    forms.forEach(form => {
        form.addEventListener('submit', function(e) {
            const password = form.querySelector('input[name="password"]');
            const confirm = form.querySelector('input[name="password_confirm"]');
            
            if (password && confirm) {
                if (password.value !== confirm.value) {
                    e.preventDefault();
                    alert("Errore: Le password non coincidono!");
                    confirm.focus();
                }
            }
        });
    });

    const nav = document.getElementById('mainNavbar');
    if (nav) {
        window.onscroll = function() {
            if (window.pageYOffset > 50) {
                nav.classList.add("scrolled");
            } else {
                nav.classList.remove("scrolled");
            }
        };
    }


    document.querySelectorAll('a[href^="#"]').forEach(anchor => {
        anchor.addEventListener('click', function (e) {
            const targetId = this.getAttribute('href');
            if (targetId === '#' || !targetId.startsWith('#')) return;
            
            const targetElement = document.querySelector(targetId);
            if (targetElement) {
                e.preventDefault();
                targetElement.scrollIntoView({
                    behavior: 'smooth'
                });
            }
        });
    });

});