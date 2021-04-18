import { html, render } from 'https://unpkg.com/lit-html?module';

class TileList extends HTMLElement {
    connectedCallback() {
        window.addEventListener('x-update-tile', e => this.updateTiles(e));
    }

    updateTiles({ detail: {payload: tiles} }) {
        const list = tiles
            .map((tile) => html`
                <li>${tile.ownersId}</li>
                    <ul>
                        <li>houses: ${tile.houses}</li>
                        <li>hotels: ${tile.hotels}</li>
                        <li>price: ${tile.price}</li>
                    </ul>`);
        render(html`<ul>Tiles:${list}</ul>`, this);
    }
}

customElements.define('tile-list', TileList);
