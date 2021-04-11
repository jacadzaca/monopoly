import { html, render } from 'https://unpkg.com/lit-html?module';

class TurnCounter extends HTMLElement {
    me = undefined;

    connectedCallback() {
        window.addEventListener('x-set-my-turn', e => this.me = e.detail.payload);
        window.addEventListener('x-turn-change', e => this.updateTurn(e));
    }

    updateTurn({ detail: {payload: currentPlayer} }) {
        let template = undefined;
        if (currentPlayer === this.me) {
            template = html`<p class="pulse">It's your turn!</p>`
        } else {
            template = html`<p>${currentPlayer} is making a move...</p>`
        }
        render(template, this);
    }
}

customElements.define('turn-counter', TurnCounter);

