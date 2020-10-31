package com.jacadzaca.monopoly.requests.parsing

import com.jacadzaca.monopoly.gamelogic.*
import com.jacadzaca.monopoly.requests.*
import java.util.*

interface RequestParser<in T> {
  companion object {
    internal val MISSING_TYPE = Result.failure<Request>(ParsingException("Json missing type [string] field"))
    internal val UNKNOWN_TYPE = Result.failure<Request>(ParsingException("Invalid value in type field"))
  }

  fun parse(raw: T, playersId: UUID, gameState: GameState): Result<Request>

  private class ParsingException(message: String) : Throwable(message)
}
