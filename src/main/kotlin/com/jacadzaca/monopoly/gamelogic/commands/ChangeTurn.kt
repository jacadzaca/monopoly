package com.jacadzaca.monopoly.gamelogic.commands

import com.jacadzaca.monopoly.gamelogic.GameState

class ChangeTurn(private val gameState: GameState) : Command {
    override fun execute(): GameState {
        return if (gameState.turnOrder.size == 0) {
            gameState
        } else {
            gameState.updateTurn((gameState.currentTurn + 1) % gameState.turnOrder.size)
        }
    }
}
