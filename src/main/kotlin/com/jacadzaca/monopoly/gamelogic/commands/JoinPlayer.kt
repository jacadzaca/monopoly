package com.jacadzaca.monopoly.gamelogic.commands

import com.jacadzaca.monopoly.gamelogic.GameState
import com.jacadzaca.monopoly.gamelogic.Player
import java.util.UUID

class JoinPlayer(
    private val newPlayer: Player,
    private val playersId: UUID,
    private val target: GameState
) : Command {
    override fun execute(): GameState {
        return target.put(playersId, newPlayer)
    }
}
