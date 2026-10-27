
document.addEventListener('DOMContentLoaded', function() {
    const caposquadraSelect = document.getElementById('caposquadraId');
    const operatorCheckboxes = document.querySelectorAll('input[name="operatoriIds"]');

    function aggiornaDisponibilitaOperatori() {
        const selectedId = caposquadraSelect.value;

        operatorCheckboxes.forEach(checkbox => {
            const container = checkbox.closest('.checkbox-item');
            
            if (checkbox.value === selectedId) {
                checkbox.checked = false; 
                checkbox.disabled = true;  
                container.style.opacity = '0.3'; 
                container.style.backgroundColor = '#ffebee'; 
                container.title = "L'utente è già selezionato come Caposquadra";
            } else {
                checkbox.disabled = false;
                container.style.opacity = '1';
                container.style.backgroundColor = 'transparent';
                container.title = "";
            }
        });
    }

    if (caposquadraSelect) {
        caposquadraSelect.addEventListener('change', aggiornaDisponibilitaOperatori);
        
        aggiornaDisponibilitaOperatori();
    }
});

// per evitare che il capo squadra venga selezionato come operatore