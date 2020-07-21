package com.jacadzaca.monopoly.gamelogic.gamestate.events

import com.jacadzaca.monopoly.gamelogic.gamestate.GameState

fun tileExists(tileIndex: Int, gameState: GameState): Boolean = gameState.boardSize > tileIndex && tileIndex > 0
