import { html, render } from 'https://unpkg.com/lit-html?module';
import { sum } from '/static/util.js'

class TileList extends HTMLElement {
    connectedCallback() {
        window.addEventListener('x-update-tile', e => this.updateTiles(e));
    }

    renderEstate(estate) {
        switch(estate.type) {
            case 'house':
                return '🏠'
                break;
            case 'hotel':
                return '🏨'
                break;
        }
    }

    computeRent(tile) {
        return BigInt(tile.baseRent) + tile.houses.concat(tile.hotels)
                          .map(house => BigInt(house.rent))
                          .reduce(sum, BigInt(0));
    }

    getTilesOwnerName(tile, gameState) {
        const owner = gameState.players[tile.ownersId];
        if (owner !== undefined) {
            return owner.name;
        } else {
            return '';
        }
    }

    updateTiles({ detail: {payload: gameState} }) {
        const list = gameState.tiles.map((tile) => html`
                <li>${this.getTilesOwnerName(tile, gameState)}</li>
                    <ul>
                        <li>houses: ${tile.houses.map(this.renderEstate)}</li>
                        <li>hotels: ${tile.hotels.map(this.renderEstate)}</li>
                        <li>price: ${tile.price}</li>
                        <li>rent: ${this.computeRent(tile)}</li>
                    </ul>`);
        render(html`<ol start="0">Tiles:${list}</ol>`, this);
    }
}

customElements.define('tile-list', TileList);

