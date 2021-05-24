package com.jacadzaca.monopoly.gamelogic.commands

import com.jacadzaca.monopoly.gamelogic.Estate
import com.jacadzaca.monopoly.gamelogic.GameState
import com.jacadzaca.monopoly.gamelogic.Player
import java.util.UUID

class BuyEstate(
    private val buyer: Player,
    private val buyersId: UUID,
    private val tileIndex: Int,
    private val estate: Estate,
    private val target: GameState
) : Command {

    override fun execute(): GameState {
        return target
            .updateTile(tileIndex, newEstate = estate)
            .updatePlayer(buyersId, newBalance = buyer.balance - estate.price)
    }
}
