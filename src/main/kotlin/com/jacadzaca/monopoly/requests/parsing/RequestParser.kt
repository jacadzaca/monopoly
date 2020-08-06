package com.jacadzaca.monopoly.requests.parsing

interface RequestParser<in T> {
  fun parse(raw: T): ParsingResult
}
