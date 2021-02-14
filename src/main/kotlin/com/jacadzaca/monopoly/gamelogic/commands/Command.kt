package com.jacadzaca.monopoly.gamelogic.commands

import com.jacadzaca.monopoly.gamelogic.GameState

interface Command {
  fun asEvent(): Event
  fun execute(): GameState
}
