document.addEventListener("DOMContentLoaded", initApp);

const BASE_URL_ORDERS = "/orders";
let currentOrder = null;

async function initApp() {
    const id = getOrderIdFromUrl();
    const orders = await fetchOrders();
    currentOrder = orders.find(o => o.id === id);

    if (!currentOrder) {
        document.querySelector("#orderTitle").textContent = "Bestilling ikke fundet";
        document.querySelector("#addLineSection").remove();
        return;
    }

    displayOrder(currentOrder);

    document.querySelector("#addLineForm").addEventListener("submit", handleAddLineSubmit);
    document.querySelector("#markAsSentButton").addEventListener("click", handleMarkAsSent);
    document.querySelector("#trackingForm").addEventListener("submit", handleTrackingSubmit);
    document.querySelector("#deliveryForm").addEventListener("submit", handleDeliverySubmit);
}

function getOrderIdFromUrl() {
    const params = new URLSearchParams(window.location.search);
    return Number(params.get("id"));
}

async function fetchOrders() {
    try {
        const response = await fetch(BASE_URL_ORDERS);
        if (!response.ok) {
            throw new Error("Kunne ikke hente bestillinger");
        }
        return await response.json();
    } catch (error) {
        console.error(error);
        return [];
    }
}

async function addOrderLine(orderId, line) {
    try {
        const response = await fetch(`${BASE_URL_ORDERS}/${orderId}/lines`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(line)
        });
        if (!response.ok) {
            await showErrorFromResponse(response);
            return null;
        }
        return await response.json();
    } catch (error) {
        console.error(error);
        alert("Der opstod en fejl - kunne ikke tilføje komponenten");
        return null;
    }
}

async function markOrderAsSent(orderId) {
    try {
        const response = await fetch(`${BASE_URL_ORDERS}/${orderId}/send`, {
            method: "PATCH"
        });
        if (!response.ok) {
            await showErrorFromResponse(response);
            return null;
        }
        return await response.json();
    } catch (error) {
        console.error(error);
        alert("Der opstod en fejl - kunne ikke markere bestillingen som sendt");
        return null;
    }
}

async function updateTrackingCode(orderId, trackingCode) {
    try {
        const response = await fetch(`${BASE_URL_ORDERS}/${orderId}/tracking`, {
            method: "PATCH",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({ trackingCode })
        });
        if (!response.ok) {
            await showErrorFromResponse(response);
            return null;
        }
        return await response.json();
    } catch (error) {
        console.error(error);
        alert("Der opstod en fejl - kunne ikke opdatere tracking-koden");
        return null;
    }
}

async function showErrorFromResponse(response) {
    const problem = await response.json();
    alert(problem.detail || "Der opstod en ukendt fejl");
}

function displayOrder(order) {
    document.querySelector("#orderTitle").textContent = `Bestilling hos ${order.supplierName}`;
    document.querySelector("#orderStatus").textContent = order.sentDate ? "Status: Sendt" : "Status: Kladde";
    document.querySelector("#trackingCode").value = order.trackingCode ?? "";
    document.querySelector("#expectedDeliveryDate").value = order.expectedDeliveryDate ?? "";

    const tableBody = document.querySelector("#orderLinesBody");
    tableBody.innerHTML = "";
    for (const line of order.orderLines) {
        renderOrderLineRow(line);
    }

    document.querySelector("#addLineSection").style.display = order.sentDate ? "none" : "block";
}

function renderOrderLineRow(line) {
    const tableBody = document.querySelector("#orderLinesBody");
    const row = document.createElement("tr");

    const nameCell = document.createElement("td");
    nameCell.textContent = line.componentName;

    const quantityCell = document.createElement("td");
    quantityCell.textContent = line.quantity;

    row.append(nameCell, quantityCell);
    tableBody.appendChild(row);
}

async function handleAddLineSubmit(event) {
    event.preventDefault();
    const form = new FormData(event.target);

    const newLine = {
        componentId: Number(form.get("componentId")),
        quantity: Number(form.get("quantity"))
    };

    const created = await addOrderLine(currentOrder.id, newLine);
    if (created) {
        renderOrderLineRow(created);
        event.target.reset();
    }
}

async function handleMarkAsSent() {
    const confirmed = confirm("Er du sikker på, at bestillingen skal markeres som sendt? Du kan ikke tilføje flere komponenter bagefter.");
    if (!confirmed) {
        return;
    }

    const updated = await markOrderAsSent(currentOrder.id);
    if (updated) {
        currentOrder = updated;
        displayOrder(currentOrder);
    }
}

async function handleTrackingSubmit(event) {
    event.preventDefault();
    const form = new FormData(event.target);
    const trackingCode = form.get("trackingCode");

    const updated = await updateTrackingCode(currentOrder.id, trackingCode);
    if (updated) {
        currentOrder = updated;
        alert("Tracking-kode opdateret");
    }
}

async function updateExpectedDeliveryDate(orderId, expectedDeliveryDate) {
    try {
        const response = await fetch(`${BASE_URL_ORDERS}/${orderId}/expected-delivery`, {
            method: "PATCH",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({ expectedDeliveryDate })
        });
        if (!response.ok) {
            await showErrorFromResponse(response);
            return null;
        }
        return await response.json();
    } catch (error) {
        console.error(error);
        alert("Der opstod en fejl - kunne ikke opdatere forventet levering");
        return null;
    }
}

async function handleDeliverySubmit(event) {
    event.preventDefault();
    const form = new FormData(event.target);
    const expectedDeliveryDate = form.get("expectedDeliveryDate");

    const updated = await updateExpectedDeliveryDate(currentOrder.id, expectedDeliveryDate);
    if (updated) {
        currentOrder = updated;
        alert("Forventet levering opdateret");
    }
}