document.onreadystatechange = function (event) {
  let socket = new WebSocket("ws://localhost:8080/jaca");
  let echoPlace = document.getElementById("test");
  let button = document.getElementById("button");
  let input = document.getElementById("input");
  socket.onopen = function (event) {
    socket.send("Hey!")
  };
  socket.onmessage = function (event) {
    echoPlace.innerText = event.data.toString()
  };
  button.onclick = function (event) {
    socket.send(input.value)
  };
};
