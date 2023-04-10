// all functions for game

let gamews; // websocket connection object
let blnGameOver = true;

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
    closeConnection();
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
            displayGameBoard(message.pieces);
        } else if (message.type == "score") {
            displayScore(message.message);
        } else if (message.type == "lives") {
            displayLives(message.message);
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
function closeConnection() {
    if (gamews!=null && chatws.readyState == 1) {
        gamews.close();
    }
}

// method for creating the JSON request and send the instructions to the server
function sendPlayerData(name,level) {
    let request = {"type":"enter", "name":name, "level":level};
    gamews.send(JSON.stringify(request));
}

function sendClick(row,column) {
    if (!blnGameOver) {
        let request = {"type": "select", "row": row, "column": column};
        gamews.send(JSON.stringify(request));
    }
}

function showMessage(type,message) {
    let msgBox = document.getElementById('messages');
    msgBox.innerHTML = "[" + type.toUpperCase() + "] " + message + "\n";
}

function displayGameBoard(pieces) {
    showMessage("game",pieces);
}

function displayScore(score) {
    let inputBox = document.getElementById('score');
    inputBox.value = score;
}

function displayLives(lives) {
    let inputBox = document.getElementById('lives');
    inputBox.value = lives;
}

function displayFailed(row,column) {

}

function gameOver(result) {
    blnGameOver = true;
    closeConnection();
}
