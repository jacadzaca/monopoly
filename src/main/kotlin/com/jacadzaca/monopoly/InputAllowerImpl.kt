package com.jacadzaca.monopoly

class InputAllowerImpl : InputAllower<NetworkPlayer> {
  override fun allowInput(from: NetworkPlayer, inputHandler: (String) -> String) {
    from.connection.textMessageHandler {
      from.connection.writeTextMessage(inputHandler(it))
    }
  }

  override fun disallowInput(from: NetworkPlayer, errorMessageSupplier: () -> String) {
    from.connection.textMessageHandler {
      from.connection.writeTextMessage(errorMessageSupplier())
    }
  }

}
