document.addEventListener("DOMContentLoaded", initApp);

const BASE_URL_ORDERS = "/orders";

async function initApp() {
    const orders = await fetchOrders();
    displayOrders(orders);
    const suppliers = await fetchSuppliers();
    displaySuppliers(suppliers);

    document.querySelector("#orderForm").addEventListener("submit", handleFormSubmit);
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

async function createOrder(order) {
    try {
        const response = await fetch(BASE_URL_ORDERS, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(order)
        });
        if (!response.ok) {
            await showErrorFromResponse(response);
            return null;
        }
        return await response.json();
    } catch (error) {
        console.error(error);
        alert("Der opstod en fejl - kunne ikke oprette bestillingen");
        return null;
    }
}

async function showErrorFromResponse(response) {
    const problem = await response.json();
    alert(problem.detail || "Der opstod en ukendt fejl");
}

function displayOrders(orders) {
    const tableBody = document.querySelector("#ordersTableBody");
    tableBody.innerHTML = "";
    const notReceived = orders.filter(order => !order.receivedDate);
    for (const order of notReceived) {
        renderOrderRow(order);
    }
}

async function markOrderAsReceived(orderId) {
    try {
        const response = await fetch(`${BASE_URL_ORDERS}/${orderId}/receive`, {
            method: "PATCH"
        });
        if (!response.ok) {
            await showErrorFromResponse(response);
            return null;
        }
        return await response.json();
    } catch (error) {
        console.error(error);
        alert("Der opstod en fejl - kunne ikke markere bestillingen som modtaget");
        return null;
    }
}

function renderOrderRow(order) {
    const tableBody = document.querySelector("#ordersTableBody");
    const row = document.createElement("tr");

    const idCell = document.createElement("td");
    idCell.textContent = order.id;

    const supplierCell = document.createElement("td");
    supplierCell.textContent = order.supplierName;

    const statusCell = document.createElement("td");
    statusCell.textContent = order.sentDate ? "Sendt" : "Kladde";

    const deliveryCell = document.createElement("td");
    deliveryCell.textContent = order.expectedDeliveryDate ?? "";

    const trackingCell = document.createElement("td");
    trackingCell.textContent = order.trackingCode ?? "";

    const actionCell = document.createElement("td");
    const link = document.createElement("a");
    link.href = `order.html?id=${order.id}`;
    link.textContent = "Åbn";
    actionCell.appendChild(link);

    if (order.sentDate) {
        const receiveButton = document.createElement("button");
        receiveButton.textContent = "Modtaget";
        receiveButton.addEventListener("click", () => handleReceiveClick(order.id));
        actionCell.appendChild(receiveButton);
    }

    row.append(idCell, supplierCell, statusCell, deliveryCell, trackingCell, actionCell);
    tableBody.appendChild(row);
}

async function handleReceiveClick(orderId) {
    const updated = await markOrderAsReceived(orderId);
    if (updated) {
        const orders = await fetchOrders();
        displayOrders(orders);
    }
}

async function handleFormSubmit(event) {
    event.preventDefault();
    const form = new FormData(event.target);

    const newOrder = {
        supplierId: Number(form.get("supplierId"))
    };

    const created = await createOrder(newOrder);
    if (created) {
        window.location.href = `order.html?id=${created.id}`;
    }
}

const BASE_URL_SUPPLIERS = "/suppliers";

async function fetchSuppliers() {
    try {
        const response = await fetch(BASE_URL_SUPPLIERS);
        if (!response.ok) {
            throw new Error("Kunne ikke hente leverandører");
        }
        return await response.json();
    } catch (error) {
        console.error(error);
        return [];
    }
}

function displaySuppliers(suppliers) {
    const tableBody = document.querySelector("#suppliersTableBody");
    tableBody.innerHTML = "";
    for (const supplier of suppliers) {
        const row = document.createElement("tr");

        const idCell = document.createElement("td");
        idCell.textContent = supplier.id;

        const nameCell = document.createElement("td");
        nameCell.textContent = supplier.name;

        row.append(idCell, nameCell);
        tableBody.appendChild(row);
    }
}