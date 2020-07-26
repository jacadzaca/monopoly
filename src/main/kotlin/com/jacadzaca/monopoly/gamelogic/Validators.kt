package com.jacadzaca.monopoly.gamelogic

import com.jacadzaca.monopoly.gamelogic.GameState

fun tileExists(tileIndex: Int, gameState: GameState): Boolean = gameState.boardSize > tileIndex && tileIndex > 0
