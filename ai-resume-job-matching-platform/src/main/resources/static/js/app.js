async function checkServer(){

    try{

        const response = await fetch("/api/v1/health");

        const data = await response.json();

        document.getElementById("status").innerHTML =
            "🟢 " + data.message;

    }

    catch(error){

        document.getElementById("status").innerHTML =
            "🔴 Backend Offline";

    }

}

checkServer();