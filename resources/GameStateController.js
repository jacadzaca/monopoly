const removeElement = (array, element) => {
    const index = array.indexOf(element);
    if (index > -1) {
        array.splice(index, 1);
    }
    return array;
}

const request = (socket, type) => {
    socket.send(`{"type": "${type}"}`);
}

// https://stackoverflow.com/questions/42304996/javascript-using-promises-on-websocket
const connect = () => {
    return new Promise( (resolve, reject) => {
        const socket = new WebSocket('ws://localhost:8081/');
        socket.onopen = () => { resolve(socket); };
        socket.onerror = (error) => { reject(error); };
    });
}

const getMessage = (socket) => {
    return new Promise( (handle) => {
        socket.onmessage = (e) => { handle(e) }
    });
}

const handleRequest = (gameState, json) => {
    console.log(json);
    switch(json.type) {
        case 'playerJoin':
            json.newPlayer.color = COLORS[gameState.turnOrder.length];
            json.newPlayer.desiredPosition = 0;
            gameState.players[json.playersId] = json.newPlayer;
            gameState.turnOrder.push(json.playersId);
            break;
        case 'playerLeave':
            delete gameState.players[json.playersId];
            gameState.turnOrder = removeElement(gameState.turnOrder, json.playersId);
            break;
        case 'turnChange':
            gameState.currentTurn = json.turn;
            break;
        case 'positionChange':
            gameState.players[json.playersId].desiredPosition = json.newPosition;
            break;
        case 'balanceChange':
            gameState.players[json.playersId].balance = json.newBalance;
            break;
        case 'tileOwnershipChange':
            gameState.tiles[json.tilesIndex].ownersId = json.newOwner;
            break;
        case 'estateAdded':
            switch(json.estate.type) {
                case 'house':
                    gameState.tiles[json.tilesIndex].houses.push(json.estate);
                    break;
                case 'hotel':
                    gameState.tiles[json.tilesIndex].hotels.push(json.estate);
                    break;
            }
            break;
        case 'nameChange':
            gameState.players[json.playersId].name = json.name;
            break;
        default:
            break;
    }
}

