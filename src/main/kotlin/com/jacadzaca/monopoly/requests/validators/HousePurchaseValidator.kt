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

internal class HousePurchaseValidator(
    private val house: Estate.House,
    private val createPurchase: (Player, UUID, Int, Estate, GameState) -> BuyEstate
) : RequestValidator<PlayerAction.BuyHouseAction> {
    override fun validate(playersId: UUID, action: PlayerAction.BuyHouseAction, context: GameState): Computation<Command> {
        val buyer = context.players[playersId] ?: return INVALID_PLAYER_ID
        val tile = context.tiles[buyer.position]
        return when {
            !context.isPlayersTurn(playersId) -> NOT_PLAYERS_TURN
            tile.ownersId != playersId -> TILE_NOT_OWNED_BY_BUYER
            house.price > buyer.balance -> BUYER_HAS_INSUFFICIENT_BALANCE
            else -> Computation.success(createPurchase(buyer, playersId, buyer.position, house, context))
        }
    }
}
