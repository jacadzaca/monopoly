import { removeElement, createEvent } from '/static/util.js';

const socket = new WebSocket('ws://localhost:8081/');
let gameState = null;

socket.onmessage = (e) => {
    gameState = JSON.parse(e.data);
    window.dispatchEvent(createEvent('x-update-list', gameState.players));
    window.dispatchEvent(createEvent('x-update-tile', gameState));

    let myTurn = new URL(window.location.href).searchParams.get('name');
    if (myTurn === null) {
        myTurn = gameState.turnOrder[gameState.turnOrder.length - 1];
    }
    socket.send(`{"type": "change-name", "newName": "${myTurn}"}`);
    window.dispatchEvent(createEvent('x-set-my-turn', myTurn));

    const playersId = gameState.turnOrder[gameState.currentTurn];
    const currentPlayer = gameState.players[playersId].name;
    if (currentPlayer !== null) {
        window.dispatchEvent(createEvent('x-turn-change', currentPlayer));
    } else {
        window.dispatchEvent(createEvent('x-turn-change', myTurn));
    }
    socket.onmessage = (request) => { handleRequest(JSON.parse(request.data)) };
}

const handleRequest = (json) => {
    switch(json.type) {
        case 'playerJoin':
            gameState.players[json.playersId] = json.newPlayer;
            gameState.turnOrder.push(json.playersId);
            window.dispatchEvent(createEvent('x-update-list', gameState.players));
            break;
        case 'playerLeave':
            delete gameState.players[json.playersId];
            gameState.turnOrder = removeElement(gameState.turnOrder, json.playersId);
            window.dispatchEvent(createEvent('x-update-list', gameState.players));
            break;
        case 'turnChange':
            const playersId = gameState.turnOrder[json.turn];
            const currentPlayer = gameState.players[playersId].name;
            window.dispatchEvent(createEvent('x-turn-change', currentPlayer));
            break;
        case 'positionChange':
            gameState.players[json.playersId].position = json.newPosition;
            window.dispatchEvent(createEvent('x-update-list', gameState.players));
            break;
        case 'balanceChange':
            gameState.players[json.playersId].balance = json.newBalance;
            window.dispatchEvent(createEvent('x-update-list', gameState.players));
            break;
        case 'tileOwnershipChange':
            gameState.tiles[json.tilesIndex].ownersId = json.newOwner;
            window.dispatchEvent(createEvent('x-update-tile', gameState));
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
            window.dispatchEvent(createEvent('x-update-tile', gameState));
            break;
        case 'nameChange':
            gameState.players[json.playersId].name = json.name;
            window.dispatchEvent(createEvent('x-update-list', gameState.players));
            break;
        default:
            break;
    }
}

const request = type => {
    socket.send(`{"type": "${type}"}`);
}

export { request };

