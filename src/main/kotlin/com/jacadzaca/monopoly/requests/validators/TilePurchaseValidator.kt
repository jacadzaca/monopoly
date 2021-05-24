package com.jacadzaca.monopoly.requests.validators

import com.jacadzaca.monopoly.Computation
import com.jacadzaca.monopoly.gamelogic.GameState
import com.jacadzaca.monopoly.gamelogic.Player
import com.jacadzaca.monopoly.gamelogic.Tile
import com.jacadzaca.monopoly.gamelogic.commands.BuyTile
import com.jacadzaca.monopoly.gamelogic.commands.Command
import com.jacadzaca.monopoly.requests.PlayerAction
import com.jacadzaca.monopoly.requests.validators.RequestValidator.Companion.BUYER_HAS_INSUFFICIENT_BALANCE
import com.jacadzaca.monopoly.requests.validators.RequestValidator.Companion.INVALID_PLAYER_ID
import com.jacadzaca.monopoly.requests.validators.RequestValidator.Companion.NOT_PLAYERS_TURN
import java.util.UUID

internal class TilePurchaseValidator(
    private val createPurchase: (Player, UUID, Tile, Int, GameState) -> (BuyTile)
) : RequestValidator<PlayerAction.BuyTileAction> {
    internal companion object {
        internal val TILE_ALREADY_HAS_OWNER =
            Computation.failure<Command>("Tile that the player wants to buy already has an owner")
    }

    override fun validate(playersId: UUID, action: PlayerAction.BuyTileAction, context: GameState): Computation<Command> {
        val buyer = context.players[playersId] ?: return INVALID_PLAYER_ID
        val tile = context.tiles[buyer.position]
        return when {
            !context.isPlayersTurn(playersId) -> NOT_PLAYERS_TURN
            tile.ownersId != null -> TILE_ALREADY_HAS_OWNER
            tile.price > buyer.balance -> BUYER_HAS_INSUFFICIENT_BALANCE
            else -> Computation.success(createPurchase(buyer, playersId, tile, buyer.position, context))
        }
    }
}
