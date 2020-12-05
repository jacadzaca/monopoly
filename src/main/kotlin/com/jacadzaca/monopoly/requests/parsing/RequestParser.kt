package com.jacadzaca.monopoly.requests.parsing

import com.jacadzaca.monopoly.*
import com.jacadzaca.monopoly.gamelogic.*
import com.jacadzaca.monopoly.requests.*
import java.util.*

interface RequestParser<in T> {
  companion object {
    internal val MISSING_TYPE = Computation.failure<Request>("Request missing type [string] field")
    internal val UNKNOWN_TYPE = Computation.failure<Request>("Invalid value in type field")
  }

  fun parse(raw: T, playersId: UUID, gameState: GameState): Computation<Request>

}
