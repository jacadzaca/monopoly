package com.jacadzaca.monopoly

interface InputAllower<T : Player> {
  fun allowInput(from: T, inputHandler: (String) -> String)
  fun disallowInput(from: T, errorMessageSupplier: () -> String)
}
