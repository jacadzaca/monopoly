package com.jacadzaca.monopoly.requests.validators


import com.jacadzaca.monopoly.*
import com.jacadzaca.monopoly.gamelogic.*
import com.jacadzaca.monopoly.gamelogic.commands.*
import com.jacadzaca.monopoly.requests.*
import com.jacadzaca.monopoly.gamelogic.*
import com.jacadzaca.monopoly.gamelogic.commands.*
import java.util.*

internal object ValidatorProxyImpl: ValidatorProxy{
  private val house = Estate.House(100.toBigInteger(), 1000.toBigInteger())
  private val hotel = Estate.Hotel(200.toBigInteger(), 2000.toBigInteger())
  private val moveValidator = PlayerMovementRequestValidator(ValidatorProxyImpl::createMove)
  private val tilePurchaseValidator = TilePurchaseValidator(::BuyTile)
  private val housePurchaseValidator = HousePurchaseValidator(house, ::BuyEstate)
  private val hotelPurchaseValidator = HotelPurchaseValidator(hotel, 5, ::BuyEstate)
  private val playerJoinValidator = PlayerJoinValidator()
  private val playerLeaveValidator = PlayerLeaveValidator()

  override fun validate(request: Request, context: GameState): Computation<Command> {
    return when (request.action) {
      is PlayerAction.MoveAction-> moveValidator.validate(request.requestersId, context)
      is PlayerAction.BuyTileAction-> tilePurchaseValidator.validate(request.requestersId, context)
      is PlayerAction.BuyHouseAction-> housePurchaseValidator.validate(request.requestersId, context)
      is PlayerAction.BuyHotelAction-> hotelPurchaseValidator.validate(request.requestersId, context)
      is PlayerAction.JoinAction -> playerJoinValidator.validate(request.requestersId, context)
      is PlayerAction.LeaveAction -> playerLeaveValidator.validate(request.requestersId, context)
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

