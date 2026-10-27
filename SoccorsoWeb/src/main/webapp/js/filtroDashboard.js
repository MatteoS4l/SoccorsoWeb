//tabella richieste

let currentSortColumn = -1;
let isAscending = true;

const priorityOrder = {
    'URGENTE': 4,
    'ALTA': 3,
    'MEDIA': 2,
    'BASSA': 1
};

function sortTable(columnIndex) {
    const table = document.getElementById("tableRichieste");
    const tbody = table.querySelector("tbody");
    const rows = Array.from(tbody.querySelectorAll("tr"));
    
    if (currentSortColumn === columnIndex) {
        isAscending = !isAscending;
    } else {
        isAscending = true;
        currentSortColumn = columnIndex;
    }

    updateSortIcons(columnIndex, isAscending);

    const sortedRows = rows.sort((a, b) => {
        let valA = getCellValue(a, columnIndex);
        let valB = getCellValue(b, columnIndex);

        if (columnIndex === 4) {
            valA = priorityOrder[valA] || 0;
            valB = priorityOrder[valB] || 0;
        }

        if (valA < valB) return isAscending ? -1 : 1;
        if (valA > valB) return isAscending ? 1 : -1;
        return 0;
    });


    tbody.innerHTML = "";
    sortedRows.forEach(row => tbody.appendChild(row));
}

function getCellValue(row, index) {
    const cell = row.children[index];

    if (cell.hasAttribute('data-value')) {
        return cell.getAttribute('data-value');
    }
    return cell.textContent.trim().toUpperCase();
}

function updateSortIcons(activeIndex, ascending) {
    const headers = document.querySelectorAll("#tableRichieste th i");
    headers.forEach((icon, index) => {
        if (index === activeIndex) {
            icon.className = ascending ? "fas fa-sort-up" : "fas fa-sort-down";
            icon.style.color = "var(--primary-color)";
        } else {
            icon.className = "fas fa-sort";
            icon.style.color = "#ccc";
        }
    });
}
