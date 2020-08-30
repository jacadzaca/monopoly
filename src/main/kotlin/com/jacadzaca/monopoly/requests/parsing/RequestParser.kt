package com.jacadzaca.monopoly.requests.parsing

import com.jacadzaca.monopoly.gamelogic.*
import com.jacadzaca.monopoly.requests.*
import java.util.*

interface RequestParser<in T> {
  fun parse(raw: T, playersId: UUID, gameState: GameState): Result<Request>
}
