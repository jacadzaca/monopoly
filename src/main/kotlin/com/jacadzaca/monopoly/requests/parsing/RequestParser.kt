package com.jacadzaca.monopoly.requests.parsing

import com.jacadzaca.monopoly.gamelogic.*
import java.util.*

interface RequestParser<in T> {
  fun parse(raw: T, playersId: UUID, gameState: GameState): ParsingResult
}
