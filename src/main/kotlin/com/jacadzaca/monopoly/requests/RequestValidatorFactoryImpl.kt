package com.jacadzaca.monopoly.requests

import com.jacadzaca.monopoly.gamelogic.*
import com.jacadzaca.monopoly.gamelogic.commands.*
import java.util.*
import kotlin.random.Random

object RequestValidatorFactoryImpl : RequestValidatorFactory {
  private val positionCalculator = PositionCalculatorImpl { Random.nextInt(1, 6) }
  private val housePrice = System.getProperty("housePrice").toBigInteger()
  private val houseRent = System.getProperty("houseRent").toBigInteger()
  private val hotelPrice = System.getProperty("hotelPrice").toBigInteger()
  private val hotelRent = System.getProperty("hotelRent").toBigInteger()
  private val requiredHousesForHotel = System.getProperty("requiredHousesForHotel").toInt()

  private val moveRequestValidator = MoveRequestValidator(::createMove)
  private val tilePurchaseValidator = TilePurchaseValidator(::BuyTile)
  private val housePurchaseValidator = HousePurchaseValidator(::BuyEstate, Estate.House(houseRent, housePrice))
  private val hotelPurchaseValidator =
    HotelPurchaseValidator(::BuyEstate, requiredHousesForHotel, Estate.Hotel(hotelRent, hotelPrice))

  private fun createMove(player: Player, playersId: UUID, gameState: GameState): MovePlayer {
    return MovePlayer(
      player,
      playersId,
      positionCalculator.calculate(player.position, gameState.tiles.size),
      gameState,
      ::PayLiability
    )
  }

  override fun validatorFor(request: Request): RequestValidator {
    return when (request) {
      Request.PLAYER_MOVE -> moveRequestValidator
      Request.TILE_PURCHASE -> tilePurchaseValidator
      Request.HOUSE_PURCHASE -> housePurchaseValidator
      Request.HOTEL_PURCHASE -> hotelPurchaseValidator
    }
  }
}
