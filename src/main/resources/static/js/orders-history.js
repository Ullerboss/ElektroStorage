document.addEventListener("DOMContentLoaded", initApp);

const BASE_URL_ORDERS = "/orders";

async function initApp() {
    const orders = await fetchOrders();
    displayHistory(orders);
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

function displayHistory(orders) {
    const tableBody = document.querySelector("#historyTableBody");
    tableBody.innerHTML = "";
    const received = orders.filter(order => order.receivedDate);
    for (const order of received) {
        renderHistoryRow(order);
    }
}

function renderHistoryRow(order) {
    const tableBody = document.querySelector("#historyTableBody");
    const row = document.createElement("tr");

    const idCell = document.createElement("td");
    idCell.textContent = order.id;

    const supplierCell = document.createElement("td");
    supplierCell.textContent = order.supplierName;

    const deliveryCell = document.createElement("td");
    deliveryCell.textContent = order.expectedDeliveryDate ?? "";

    const receivedCell = document.createElement("td");
    receivedCell.textContent = order.receivedDate;

    const trackingCell = document.createElement("td");
    trackingCell.textContent = order.trackingCode ?? "";

    const actionCell = document.createElement("td");
    const link = document.createElement("a");
    link.href = `order.html?id=${order.id}`;
    link.textContent = "Åbn";
    actionCell.appendChild(link);

    row.append(idCell, supplierCell, deliveryCell, receivedCell, trackingCell, actionCell);
    tableBody.appendChild(row);
}