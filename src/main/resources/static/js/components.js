document.addEventListener("DOMContentLoaded", initApp);

const BASE_URL_COMPONENTS = "/components";

async function initApp() {
    const components = await fetchComponents();
    displayComponents(components);

    document.querySelector("#componentForm").addEventListener("submit", handleFormSubmit);
    document.querySelector("#componentTableBody").addEventListener("click", handleTableClick);
}

async function fetchComponents() {
    try {
        const response = await fetch(BASE_URL_COMPONENTS);
        if (!response.ok) {
            throw new Error("Kunne ikke hente komponenter");
        }
        return await response.json();
    } catch (error) {
        console.error(error);
        return [];
    }
}

async function addComponent(component) {
    try {
        const response = await fetch(BASE_URL_COMPONENTS, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(component)
        });
        if (!response.ok) {
            await showErrorFromResponse(response);
            return null;
        }
        return await response.json();
    } catch (error) {
        console.error(error);
        alert("Der opstod en fejl - kunne ikke oprette komponenten");
        return null;
    }
}

async function discontinueComponent(id) {
    try {
        const response = await fetch(`${BASE_URL_COMPONENTS}/${id}/discontinue`, {
            method: "PATCH"
        });
        if (!response.ok) {
            await showErrorFromResponse(response);
            return null;
        }
        return await response.json();
    } catch (error) {
        console.error(error);
        alert("Der opstod en fejl - kunne ikke markere komponenten som udgået");
        return null;
    }
}

async function reactivateComponent(id) {
    try {
        const response = await fetch(`${BASE_URL_COMPONENTS}/${id}/reactivate`, {
            method: "PATCH"
        });
        if (!response.ok) {
            await showErrorFromResponse(response);
            return null;
        }
        return await response.json();
    } catch (error) {
        console.error(error);
        alert("Der opstod en fejl - kunne ikke fortryde");
        return null;
    }
}

async function showErrorFromResponse(response) {
    const problem = await response.json();
    alert(problem.detail || "Der opstod en ukendt fejl");
}

function displayComponents(components) {
    const tableBody = document.querySelector("#componentTableBody");
    tableBody.innerHTML = "";
    for (const component of components) {
        renderComponentRow(component);
    }
}

function renderComponentRow(component) {
    const tableBody = document.querySelector("#componentTableBody");
    const row = buildComponentRow(component);
    tableBody.appendChild(row);
}

function buildComponentRow(component) {

    const row = document.createElement("tr");
    row.setAttribute("data-id", component.id);

    const idCell = document.createElement("td");
    idCell.textContent = component.id;

    const nameCell = document.createElement("td");
    nameCell.textContent = component.name;

    const supplierCell = document.createElement("td");
    supplierCell.textContent = component.supplierName;

    const partNumberCell = document.createElement("td");
    partNumberCell.textContent = component.externalPartNumber;

    const statusCell = document.createElement("td");
    statusCell.textContent = component.discontinued ? "Udgået" : "Aktiv";

    const actionCell = document.createElement("td");
    const actionButton = document.createElement("button");
    if (component.discontinued) {
        actionButton.textContent = "Fortryd";
        actionButton.setAttribute("data-action", "reactivate");
    } else {
        actionButton.textContent = "Marker som udgået";
        actionButton.setAttribute("data-action", "discontinue");
    }
    actionCell.appendChild(actionButton);

    row.append(idCell, nameCell, supplierCell, partNumberCell, statusCell, actionCell);
    return row;
}

async function handleFormSubmit(event) {
    event.preventDefault();
    const form = new FormData(event.target);

    const newComponent = {
        name: form.get("name"),
        supplierId: Number(form.get("supplierId")),
        externalPartNumber: form.get("externalPartNumber")
    };

    const created = await addComponent(newComponent);
    if (created) {
        renderComponentRow(created);
        event.target.reset();
    }
}

async function handleTableClick(event) {
    const action = event.target.getAttribute("data-action");
    if (action !== "discontinue" && action !== "reactivate") {
        return;
    }

    const row = event.target.closest("tr");
    const id = row.getAttribute("data-id");

    if (action === "discontinue") {
        const confirmed = confirm("Er du sikker på, at komponenten skal markeres som udgået?");
        if (!confirmed) {
            return;
        }
        const updated = await discontinueComponent(id);
        if (updated) {
            row.replaceWith(buildComponentRow(updated));
        }
    } else {
        const updated = await reactivateComponent(id);
        if (updated) {
            row.replaceWith(buildComponentRow(updated));
        }
    }
}