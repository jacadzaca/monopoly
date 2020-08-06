package com.jacadzaca.monopoly.requests.parsing

import com.jacadzaca.monopoly.requests.Request

sealed class ParsingResult {
  data class Success(val request: Request): ParsingResult()
  data class Failure(val reason: String): ParsingResult()
}
