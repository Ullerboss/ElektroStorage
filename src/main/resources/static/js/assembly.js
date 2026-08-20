document.addEventListener("DOMContentLoaded", initApp);

const BASE_URL_ASSEMBLIES = "/assemblies";

async function initApp() {
    const id = getAssemblyIdFromUrl();
    const assemblies = await fetchAssemblies();
    const assembly = assemblies.find(a => a.id === id);

    if (!assembly) {
        document.querySelector("#assemblyTitle").textContent = "Stykliste ikke fundet";
        return;
    }

    displayAssembly(assembly);
}

function getAssemblyIdFromUrl() {
    const params = new URLSearchParams(window.location.search);
    return Number(params.get("id"));
}

async function fetchAssemblies() {
    try {
        const response = await fetch(BASE_URL_ASSEMBLIES);
        if (!response.ok) {
            throw new Error("Kunne ikke hente styklister");
        }
        return await response.json();
    } catch (error) {
        console.error(error);
        return [];
    }
}

function displayAssembly(assembly) {
    document.querySelector("#assemblyTitle").textContent = assembly.resultComponentName;

    const tableBody = document.querySelector("#assemblyItemsBody");
    tableBody.innerHTML = "";
    for (const item of assembly.items) {
        const row = document.createElement("tr");

        const nameCell = document.createElement("td");
        nameCell.textContent = item.componentName;

        const quantityCell = document.createElement("td");
        quantityCell.textContent = item.quantity;

        row.append(nameCell, quantityCell);
        tableBody.appendChild(row);
    }
}