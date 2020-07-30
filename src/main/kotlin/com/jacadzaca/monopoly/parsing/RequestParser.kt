package com.jacadzaca.monopoly.parsing

interface RequestParser<in T> {
  fun parse(raw: T): ParsingResult
}
