package com.jacadzaca.monopoly

import io.vertx.core.http.ServerWebSocket

interface InputAllower {
  fun allowInput(from: ServerWebSocket, inputHandler: (String) -> String)
  fun disallowInput(from: ServerWebSocket, errorMessageSupplier: () -> String)
}
