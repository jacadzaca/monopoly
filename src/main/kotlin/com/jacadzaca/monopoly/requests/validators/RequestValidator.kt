package com.jacadzaca.monopoly.requests.validators
import com.jacadzaca.monopoly.Computation
import com.jacadzaca.monopoly.gamelogic.GameState
import com.jacadzaca.monopoly.gamelogic.commands.Command
import com.jacadzaca.monopoly.requests.PlayerAction
import java.util.UUID

interface RequestValidator<T : PlayerAction> {
    companion object {
        internal val INVALID_PLAYER_ID = Computation.failure<Command>("No player with specified ID exist")
        internal val BUYER_HAS_INSUFFICIENT_BALANCE =
            Computation.failure<Command>("Buyer dose not have enough funds to preform requested action")
        internal val TILE_NOT_OWNED_BY_BUYER =
            Computation.failure<Command>("Buyer dose not own the tile where he wants to buy a estate")
        internal val NOT_PLAYERS_TURN = Computation.failure<Command>("It is not the player's turn")
    }

    fun validate(playersId: UUID, action: T, context: GameState): Computation<Command>
}
