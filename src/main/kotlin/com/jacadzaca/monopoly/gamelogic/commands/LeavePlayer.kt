package com.jacadzaca.monopoly.gamelogic.commands

import com.jacadzaca.monopoly.gamelogic.GameState
import java.util.UUID

data class LeavePlayer(
    private val playersId: UUID,
    private val reason: String,
    private val gameState: GameState,
) : Command {
    override fun execute(): GameState {
        val changedGameState = gameState.remove(playersId, reason).disownPlayer(playersId)
        return if (gameState.isPlayersTurn(playersId)) {
            ChangeTurn(changedGameState).execute()
        } else {
            changedGameState
        }
    }
}
