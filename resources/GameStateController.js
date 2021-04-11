const socket = new WebSocket('ws://localhost:8081/');
let gameState = undefined;

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
}

const handleRequest = json => {
    console.log(json);
    switch(json.type) {
        case 'player-joined':
            gameState.players[json.playersId] = {position: 0, balance: 0};
            window.dispatchEvent(createEvent('x-update-list', gameState.players));
            break;
        case 'player-left':
            delete gameState.players[json.playersId];
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

