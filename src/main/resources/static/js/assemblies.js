document.addEventListener("DOMContentLoaded", initApp);

const BASE_URL_ASSEMBLIES = "/assemblies";

async function initApp() {
    const assemblies = await fetchAssemblies();
    displayAssemblies(assemblies);
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

function displayAssemblies(assemblies) {
    const list = document.querySelector("#assembliesList");
    list.innerHTML = "";
    for (const assembly of assemblies) {
        const item = document.createElement("li");
        const link = document.createElement("a");
        link.href = `assembly.html?id=${assembly.id}`;
        link.textContent = assembly.resultComponentName;
        item.appendChild(link);
        list.appendChild(item);
    }
}