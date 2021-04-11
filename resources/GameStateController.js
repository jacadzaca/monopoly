const socket = new WebSocket('ws://localhost:8081/');
let gameState = undefined;

const removeElement = (array, element) => {
    const index = array.indexOf(element);
    if (index > -1) {
        array.splice(index, 1);
    }
    return array;
}

const createEvent = (name, payload) => {
    return new CustomEvent(name, {
        detail: {
            payload: payload
        },
        bubbles: true
    });
}

socket.onmessage = event => {
    gameState = JSON.parse(event.data);
    console.log(gameState);
    socket.onmessage = e => { handleRequest(JSON.parse(e.data)) };
    window.dispatchEvent(createEvent('x-update-list', gameState.players));

    const myTurn = gameState.turnOrder[gameState.turnOrder.length - 1]
    window.dispatchEvent(createEvent('x-set-my-turn', myTurn))

    const currentPlayer = gameState.turnOrder[gameState.currentTurn]
    window.dispatchEvent(createEvent('x-turn-change', currentPlayer))
}

const handleRequest = json => {
    console.log(json);
    switch(json.type) {
        case 'player-joined':
            gameState.players[json.playersId] = {position: 0, balance: 1000};
            gameState.turnOrder.push(json.playersId);
            window.dispatchEvent(createEvent('x-update-list', gameState.players));
            break;
        case 'player-left':
            delete gameState.players[json.playersId];
            gameState.turnOrder = removeElement(gameState.turnOrder, json.playersId);
            window.dispatchEvent(createEvent('x-update-list', gameState.players));
            break;
        case 'turn-change':
            gameState.currentTurn += 1;
            gameState.currentTurn %= gameState.turnOrder.length;
            const currentPlayer = gameState.turnOrder[gameState.currentTurn]
            window.dispatchEvent(createEvent('x-turn-change', currentPlayer))
            break;
        case 'moved':
            gameState.players[json.playersId].position = json.newPosition;
            window.dispatchEvent(createEvent('x-update-list', gameState.players));
            break;
        default:
            break;
    }
}

const request = type => {
    socket.send(`"${type}"`);
}

export { request };

