document.addEventListener("DOMContentLoaded", initApp);

const BASE_URL_INVENTORY = "/inventory";

async function initApp() {
    const received = await fetchReceivedComponents();
    displayReceivedComponents(received);

    document.querySelector("#countForm").addEventListener("submit", handleCountFormSubmit);
}

async function fetchReceivedComponents() {
    try {
        const response = await fetch(BASE_URL_INVENTORY);
        if (!response.ok) {
            throw new Error("Kunne ikke hente modtagne komponenter");
        }
        return await response.json();
    } catch (error) {
        console.error(error);
        return [];
    }
}

async function submitCount(count) {
    try {
        const response = await fetch(`${BASE_URL_INVENTORY}/counts`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(count)
        });
        if (!response.ok) {
            await showErrorFromResponse(response);
            return null;
        }
        return await response.json();
    } catch (error) {
        console.error(error);
        alert("Der opstod en fejl - kunne ikke indsende optællingen");
        return null;
    }
}

async function showErrorFromResponse(response) {
    const problem = await response.json();
    alert(problem.detail || "Der opstod en ukendt fejl");
}

function displayReceivedComponents(components) {
    const tableBody = document.querySelector("#inventoryTableBody");
    tableBody.innerHTML = "";
    for (const component of components) {
        renderReceivedRow(component);
    }
}

function renderReceivedRow(component) {
    const tableBody = document.querySelector("#inventoryTableBody");
    const row = document.createElement("tr");

    const idCell = document.createElement("td");
    idCell.textContent = component.componentId;

    const nameCell = document.createElement("td");
    nameCell.textContent = component.componentName;

    const quantityCell = document.createElement("td");
    quantityCell.textContent = component.quantity;

    const dateCell = document.createElement("td");
    dateCell.textContent = component.date;

    const countedByCell = document.createElement("td");
    countedByCell.textContent = component.countedBy ?? "";

    row.append(idCell, nameCell, quantityCell, dateCell, countedByCell);
    tableBody.appendChild(row);
}


async function handleCountFormSubmit(event) {
    event.preventDefault();
    const form = new FormData(event.target);

    const newCount = {
        componentId: Number(form.get("componentId")),
        countedQuantity: Number(form.get("countedQuantity")),
        countedBy: form.get("countedBy")
    };

    const created = await submitCount(newCount);
    if (created) {
        event.target.reset();
        const received = await fetchReceivedComponents();
        displayReceivedComponents(received);
    }
}