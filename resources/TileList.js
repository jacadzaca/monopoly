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
        return tile.houses.concat(tile.hotels)
                          .map(house => BigInt(house.rent))
                          .reduce(sum, BigInt(0));
    }

    updateTiles({ detail: {payload: tiles} }) {
        const list = tiles.map((tile) => html`
                <li>${tile.ownersId}</li>
                    <ul>
                        <li>houses: ${tile.houses.map(this.renderEstate)}</li>
                        <li>hotels: ${tile.hotels.map(this.renderEstate)}</li>
                        <li>price: ${tile.price}</li>
                        <li>rent: ${this.computeRent(tile)}</li>
                    </ul>`);
        render(html`<ul>Tiles:${list}</ul>`, this);
    }
}

customElements.define('tile-list', TileList);

