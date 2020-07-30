package com.jacadzaca.monopoly.parsing

import com.jacadzaca.monopoly.gamelogic.Request

sealed class ParsingResult {
  data class Success(val request: Request): ParsingResult()
  data class Failure(val reason: String): ParsingResult()
}
