package com.jacadzaca.monopoly.requests.validators

import com.jacadzaca.monopoly.gamelogic.*
import com.jacadzaca.monopoly.gamelogic.commands.*
import com.jacadzaca.monopoly.requests.*
import java.util.*

internal object ValidatorFactoryImpl : ValidatorFactory {
  private val house = Estate.House(100.toBigInteger(), 1000.toBigInteger())
  private val hotel = Estate.Hotel(200.toBigInteger(), 2000.toBigInteger())
  private val moveValidator = PlayerMovementRequestValidator(ValidatorFactoryImpl::createMove)
  private val tilePurchaseValidator = TilePurchaseValidator(::BuyTile)
  private val housePurchaseValidator = HousePurchaseValidator(house, ::BuyEstate)
  private val hotelPurchaseValidator = HotelPurchaseValidator(hotel, 5, ::BuyEstate)
  private val playerJoinValidator = PlayerJoinValidator()
  private val playerLeaveValidator = PlayerLeaveValidator()

  override fun validatorFor(action: PlayerAction): RequestValidator {
    return when (action) {
      PlayerAction.MOVE -> moveValidator
      PlayerAction.BUY_TILE -> tilePurchaseValidator
      PlayerAction.BUY_HOUSE -> housePurchaseValidator
      PlayerAction.BUY_HOTEL -> hotelPurchaseValidator
      PlayerAction.JOIN -> playerJoinValidator
      PlayerAction.LEAVE -> playerLeaveValidator
    }
  }

  private fun createMove(player: Player, playersId: UUID, gameState: GameState): MovePlayer {
    return MovePlayer(
      player,
      playersId,
      PositionCalculator.instance.calculate(player.position, gameState.tiles.size),
      gameState,
      ::PayLiability
    )
  }
}
