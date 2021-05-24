package com.jacadzaca.monopoly.requests.validators

import com.jacadzaca.monopoly.Computation
import com.jacadzaca.monopoly.gamelogic.Estate
import com.jacadzaca.monopoly.gamelogic.GameState
import com.jacadzaca.monopoly.gamelogic.Player
import com.jacadzaca.monopoly.gamelogic.commands.BuyEstate
import com.jacadzaca.monopoly.gamelogic.commands.Command
import com.jacadzaca.monopoly.requests.PlayerAction
import com.jacadzaca.monopoly.requests.validators.RequestValidator.Companion.BUYER_HAS_INSUFFICIENT_BALANCE
import com.jacadzaca.monopoly.requests.validators.RequestValidator.Companion.INVALID_PLAYER_ID
import com.jacadzaca.monopoly.requests.validators.RequestValidator.Companion.NOT_PLAYERS_TURN
import com.jacadzaca.monopoly.requests.validators.RequestValidator.Companion.TILE_NOT_OWNED_BY_BUYER
import java.util.UUID

internal class HotelPurchaseValidator(
    private val estate: Estate,
    private val requiredHousesForHotel: Int,
    private val createPurchase: (Player, UUID, Int, Estate, GameState) -> BuyEstate,
) : RequestValidator<PlayerAction.BuyHotelAction> {
    internal companion object {
        internal val NOT_ENOUGH_HOUSES =
            Computation.failure<Command>("There are not enough houses on the tile where a hotel is to be placed")
    }

    override fun validate(playersId: UUID, action: PlayerAction.BuyHotelAction, context: GameState): Computation<Command> {
        val buyer = context.players[playersId] ?: return INVALID_PLAYER_ID
        val tile = context.tiles[buyer.position]
        return when {
            !context.isPlayersTurn(playersId) -> NOT_PLAYERS_TURN
            tile.ownersId != playersId -> TILE_NOT_OWNED_BY_BUYER
            estate.price > buyer.balance -> BUYER_HAS_INSUFFICIENT_BALANCE
            tile.houseCount() < requiredHousesForHotel -> NOT_ENOUGH_HOUSES
            else -> Computation.success(createPurchase(buyer, playersId, buyer.position, estate, context))
        }
    }
}
