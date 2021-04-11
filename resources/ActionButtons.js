import { request } from './GameStateController.js'
import { html, render } from 'https://unpkg.com/lit-html?module';

class ActionButtons extends HTMLElement {
    connectedCallback() {
        const template = html`
            <button @click=${_ => request('move')}>Move</button>
            <button @click=${_ => request('buy-tile')}>Buy tile</button>
            <button @click=${_ => request('buy-house')}>Buy house</button>
            <button @click=${_ => request('buy-hotel')}>Buy hotel</button>
        `;
        render(template, this);
    }
}

customElements.define('action-buttons', ActionButtons);

