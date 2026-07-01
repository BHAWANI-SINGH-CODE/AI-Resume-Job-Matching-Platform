async function checkServer() {

    const status = document.getElementById("status");

    if (!status) {
        console.warn("Status element not found.");
        return;
    }

    try {

        const response = await fetch("/api/v1/health");
        const data = await response.json();

        status.innerHTML = "🟢 " + data.message;

    } catch (error) {

        status.innerHTML = "🔴 Backend Offline";

    }
}

checkServer();