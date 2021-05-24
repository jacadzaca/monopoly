package com.jacadzaca.monopoly.gamelogic.commands

import com.jacadzaca.monopoly.gamelogic.GameState
import java.util.UUID

class ChangeName(
    private val playersId: UUID,
    private val name: String,
    private val target: GameState,
) : Command {
    override fun execute(): GameState {
        return target.updatePlayer(playersId, newName = name)
    }
}
