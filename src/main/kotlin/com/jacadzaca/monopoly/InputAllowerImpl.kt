package com.jacadzaca.monopoly

import io.vertx.core.http.ServerWebSocket

class InputAllowerImpl : InputAllower {
  override fun allowInput(from: ServerWebSocket, inputHandler: (String) -> String) {
    from.textMessageHandler {
      from.writeTextMessage(inputHandler(it))
    }
  }

  override fun disallowInput(from: ServerWebSocket, errorMessageSupplier: () -> String) {
    from.textMessageHandler {
      from.writeTextMessage(errorMessageSupplier())
    }
  }

}
