import { html, render } from 'https://unpkg.com/lit-html?module';

class PlayerList extends HTMLElement {
    connectedCallback() {
        window.addEventListener('x-update-list', e => this.updateList(e));
    }

    updateList({ detail: {payload: players} }) {
        const list = Object
            .entries(players)
            .map(([id, {position, balance}]) => html`
                <li>${id}</li>
                    <ul>
                        <li>position: ${position}</li>
                        <li>balance: ${balance}</li>
                    </ul>`);
        render(html`<ul>${list}</ul>`, this);
    }
}

customElements.define('player-list', PlayerList);

