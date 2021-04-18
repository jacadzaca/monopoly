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

const sum = (a, b) => a + b;

export { removeElement, createEvent, sum };

