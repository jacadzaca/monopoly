const COLORS = [6274131, 2377554, 1786961, 2154278, 6185493, 8585013, 430092, 9177724, 5021765, 6710071, 4752626, 1335966, 5795058, 5416753, 5215167, 2386803, 8799941, 2076541, 5034462, 721974];

class GameScene extends Phaser.Scene {
    constructor(gameState, width, height, tileWidth, tileHeight) {
        super('GameScene');
        this.gameState = gameState;
        this.me = gameState.turnOrder[gameState.turnOrder.length - 1];
        this.width = width;
        this.height = height;
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
        this.houseRent = 100;
        this.hotelRent = 200;
    }

    createUI() {
        let y = 0;
        ['move', 'buy-tile', 'buy-house', 'buy-hotel'].forEach( (action) => {
            y += this.add
                .text(1.3 * this.tileWidth, this.height - 1.3 * this.tileHeight - 1.5 * y, `>${action}`, { fill: '#0f0' })
                .setInteractive()
                .on('pointerdown', () => { this.gameState.request(action); })
                .height;
        });

        this.turnText = this
            .add
            .text(1.3 * this.tileWidth, 1.3 * this.tileHeight, '|Not your turn|', {fill: '#0f0'});

        this.balanceText = this
            .add
            .text(3.0 * this.tileWidth, 1.3 * this.tileHeight, 'Balance:', {fill: '#0f0'});
    }

    create() {
        this.graphics = this.add.graphics();

        const rects = Array
            .from({length: (this.width / this.tileWidth)}, (_, i) => i)
            .map( (i) => new Phaser.Geom.Rectangle(i * this.tileWidth, 0, this.tileWidth, this.tileHeight))
            .concat(Array
                .from({length: (this.height / this.tileHeight) - 1 - 1 }, (_, i) => i + 1)
                .map( (i) => new Phaser.Geom.Rectangle(this.width - this.tileWidth, i * this.tileHeight, this.tileWidth, this.tileHeight)))
            .concat(Array
                .from({length: (this.width / this.tileWidth)}, (_, i) => i + 1)
                .map( (i) => new Phaser.Geom.Rectangle(this.width - i * this.tileWidth, this.height - this.tileHeight, this.tileWidth, this.tileHeight)))
            .concat(Array
                .from({length: (this.height / this.tileHeight) - 2}, (_, i) => i + 2)
                .map ( (i) => new Phaser.Geom.Rectangle(0, this.height - i * this.tileHeight, this.tileWidth, this.tileHeight)));

        this.gameState.tiles.forEach( (tile, i) => {
            tile.rect = rects[i];
            const height = this
                .add
                .text(tile.rect.x, tile.rect.y + 80, `|p${tile.price}|`, { fill: '#0f0' })
                .height;
            tile.rentText = this
                .add
                .text(tile.rect.x, tile.rect.y + 80 - height, '|r|', { fill: '#0f0' });
        });

        Object.values(this.gameState.players).forEach( (player) => {
            player.desiredPosition = player.position;
        });
        this.gameState.turnOrder.forEach( (playerId, i) => {
            this.gameState.players[playerId].color = COLORS[i];
        });

        this.time.addEvent({
            delay: 290,
            callback: () => {
                Object
                    .values(this.gameState.players)
                    .filter( (player) => player.desiredPosition != player.position)
                    .forEach( (player) => {
                        player.position = (player.position + 1) % this.gameState.tiles.length;
                    });
            },
            loop: true,
        });
        this.createUI();
    }

    update() {
        this.graphics.clear();

        this.balanceText.setText(`|Balance: ${this.gameState.players[this.me].balance}|`);

        if (this.gameState.turnOrder[this.gameState.currentTurn] === this.me) {
            this.turnText.setText("It's your turn!|");
        } else {
            this.turnText.setText('|Not your turn|');
        }

        this.gameState.tiles.forEach( (tile) => {
            if (tile.ownersId !== null) {
                this.graphics.lineStyle(5, this.gameState.players[tile.ownersId].color, 1);
            } else {
                this.graphics.lineStyle(5, 0xffffff, 1);
            }
            this.graphics.strokeRectShape(tile.rect);
            tile.rentText
                .setText(`|r${this.hotelRent * tile.hotels.length + this.houseRent * tile.houses.length + tile.baseRent}|`);
        });

        Object.values(this.gameState.players).forEach( (player, i) => {
            const tile = this.gameState.tiles[player.position];
            this.graphics.fillStyle(player.color);
            this.graphics.fillRectShape(new Phaser.Geom.Rectangle(tile.rect.x + 10, tile.rect.y + 10, this.tileWidth - 20, this.tileHeight - 20));
        });
    }
}

window.GameScene = GameScene;

