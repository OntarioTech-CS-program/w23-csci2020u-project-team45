// all functions for game

let gamews; // websocket connection object
let blnGameOver = true;
let blnFreeze = false;

//function to link home button to the home page
function homeButton(){
   window.location.href = "https://www.google.com/"; //placeholder link
}


function startGame() {
    let name = document.getElementById('name').value;
    let level = document.getElementById('level').value;
    if (name === null || name.trim().length === 0 || level === null || level.length === 0) {
        showMessage("info","Please enter name and select difficulty level to begin.");
    } else {
        enterGame();
        waitForSocketConnection(function(){
            sendPlayerData(name,level);
            blnGameOver = false;
        });
    }
}

// Make the function wait until the connection is made...
function waitForSocketConnection(callback){
    setTimeout(
        function () {
            if (gamews.readyState === 1) {
                console.log("Connection is made")
                if (callback != null){
                    callback();
                }
            } else {
                console.log("wait for connection...")
                waitForSocketConnection(callback);
            }

        }, 500); // wait 500 milisecond for the connection...
}

// method to enter a new or existing room
function enterGame(){
    closeGameConnection();
    // create the web socket
    gamews = new WebSocket("ws://localhost:8080/SnowbotRush-1.0-SNAPSHOT/ws/game/");

    // parse messages received from the server and update the UI accordingly
    gamews.onmessage = function (event) {
        console.log(event.data);
        // parsing the server's message as json
        let message = JSON.parse(event.data);

        // handle message
        if (message.type == "info") {
            showMessage(message.type,message.message);
        } else if (message.type == "error") {
            showMessage(message.type,message.message);
        } else if (message.type == "game") {
            displayGameBoard(message.message);
        } else if (message.type == "points" || message.type == "lives" || message.type == "freeze") {
            changeImage(message);
        } else if (message.type == "failed") {
            showMessage(message.type,message.message);
            displayFailed(message.row,message.column);
        } else if (message.type == "lost" || message.type == "winner") {
            showMessage(message.type,message.message);
            gameOver(message.type)
        }
    };

    gamews.onclose = function() {
        // websocket is closed.
        blnGameOver = true;
    };
}

// method invoked when connection is closed
function closeGameConnection() {
    if (gamews!=null && gamews.readyState == 1) {
        gamews.close();
    }
}

// method for creating the JSON request and send the instructions to the server
function sendPlayerData(name,level) {
    let request = {"type":"enter", "name":name, "level":level};
    gamews.send(JSON.stringify(request));
}

function sendClick(row,column) {
    if (!blnGameOver && !blnFreeze) {
        let request = {"type": "select", "row": row, "column": column};
        gamews.send(JSON.stringify(request));
    }
}

function showMessage(type,message) {
    let msgBox = document.getElementById('messages');
    msgBox.innerHTML = "[" + type.toUpperCase() + "] " + message + "\n";
}

function displayGameBoard(size) {
    let gameBoard = document.getElementById("gameBoard");

    const table = document.createElement("table");
    let imgSize = 50 - (5*(size-10));

    for (let i = 0; i < size; i++) {
        let newRow = table.insertRow(-1);
        for (let j = 0; j < size; j++) {
            let pieceCell = newRow.insertCell(j);
            pieceCell.setAttribute("align", "center");
            pieceCell.setAttribute("id","cell_"+i+"_"+j);
            let position = (i*size)+j;
            let pieceImage = document.createElement("img");
            pieceImage.src = "assets/cover.png";
            pieceImage.width = imgSize;
            pieceImage.height = imgSize;
            pieceImage.setAttribute("onclick", "sendClick('"+i+"','"+j+"')");
            pieceImage.setAttribute("id","cover_"+i+"_"+j);
            pieceCell.appendChild(pieceImage);
        }
    }
    gameBoard.innerHTML = "";
    gameBoard.appendChild(table);
}

function changeImage(data) {
    if (data.type == "points" || data.type == "lives") {
        let inputBox = document.getElementById(data.type);
        inputBox.value = data.message;
    }
    if (data.piece) {
        let img = document.getElementById("cover_"+data.row+"_"+data.column);
        img.src = "assets/"+data.piece+".png";
        img.width = img.width - 12;
        img.height = img.height - 12;
        img.removeAttribute("onclick");
        let msgText = data.value;
        if (data.type == "points") {
            msgText += " points";
        } else if (data.type == "lives"){
            msgText += " life";
        } else {
            msgText = "frozen";
        }
        let cell = document.getElementById("cell_"+data.row+"_"+data.column);
        let divCell = document.createElement("div");
        divCell.setAttribute("class","centered");
        divCell.innerHTML = msgText;
        cell.appendChild(divCell);
        let snowbot = document.getElementById("snowbot");
        let glow = "green";
        let timeout = 500;
        if (data.value == 1) {
            glow = "blue";
        } else if (data.value == -1) {
            glow = "red";
        } else if (data.type == "freeze") {
            glow = "freeze";
            msgText = "You are now frozen for "+data.value+" seconds";
            timeout = data.value*1000;
            blnFreeze = true;
        }
        snowbot.src="assets/snowbot_"+glow+".png";
        setTimeout(
            function () {
                snowbot.src="assets/snowbot.png";
                blnFreeze = false;
            }, timeout);
        showMessage("info",msgText);
    }
}

function displayFailed(row,column) {
    let img = document.getElementById("cover_"+row+"_"+column);
    img.src = "assets/claimed.png";
    img.removeAttribute("onclick");
}

function gameOver(result) {
    blnGameOver = true;
    closeGameConnection();
}
