import { removeElement, createEvent } from '/static/util.js';

const socket = new WebSocket('ws://localhost:8081/');
let gameState = null;

socket.onmessage = (e) => {
    gameState = JSON.parse(e.data);
    window.dispatchEvent(createEvent('x-update-list', gameState.players));
    window.dispatchEvent(createEvent('x-update-tile', gameState.tiles));

    const myTurn = gameState.turnOrder[gameState.turnOrder.length - 1]
    window.dispatchEvent(createEvent('x-set-my-turn', myTurn))

    const currentPlayer = gameState.turnOrder[gameState.currentTurn]
    window.dispatchEvent(createEvent('x-turn-change', currentPlayer))
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
            const currentPlayer = gameState.turnOrder[json.turn];
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
            window.dispatchEvent(createEvent('x-update-tile', gameState.tiles));
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
            window.dispatchEvent(createEvent('x-update-tile', gameState.tiles));
            break;
        default:
            break;
    }
}

const request = type => {
    socket.send(`"${type}"`);
}

export { request };

