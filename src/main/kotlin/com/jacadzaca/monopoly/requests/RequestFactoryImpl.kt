package com.jacadzaca.monopoly.requests

import com.jacadzaca.monopoly.gamelogic.*
import com.jacadzaca.monopoly.gamelogic.commands.*
import java.util.*

object RequestFactoryImpl : RequestFactory {
  private val houseRent = System.getProperty("houseRent").toInt().toBigInteger()
  private val hotelRent = System.getProperty("hotelRent").toInt().toBigInteger()
  private val housePrice = System.getProperty("housePrice").toInt().toBigInteger()
  private val hotelPrice = System.getProperty("hotelPrice").toInt().toBigInteger()

  override fun playerMoveRequest(playersId: UUID, gameState: GameState): PlayerMovementRequest {
    return PlayerMovementRequest(playersId, ::createMove, gameState)
  }

  private fun createMove(player: Player, playersId: UUID, gameState: GameState): MovePlayer {
    return MovePlayer(
      player,
      playersId,
      PositionCalculator.instance.calculate(player.position, gameState.tiles.size),
      gameState,
      ::PayLiability)
  }

  override fun tilePurchaseRequest(playersId: UUID, gameState: GameState): TilePurchaseRequest {
    return TilePurchaseRequest(playersId, gameState, ::BuyTile)
  }

  override fun housePurchaseRequest(playersId: UUID, gameState: GameState): EstatePurchaseRequest {
    return EstatePurchaseRequest(
      playersId,
      Estate.House(houseRent, housePrice),
      -1,
      ::BuyEstate,
      gameState
    )
  }

  override fun hotelPurchaseRequest(playersId: UUID, gameState: GameState): EstatePurchaseRequest {
    return EstatePurchaseRequest(
      playersId,
      Estate.Hotel(hotelRent, hotelPrice),
      4,
      ::BuyEstate,
      gameState
    )
  }
}
