package com.jacadzaca.monopoly.gamelogic.estatepurchase

import com.jacadzaca.monopoly.gamelogic.GameState
import com.jacadzaca.monopoly.gamelogic.Player
import com.jacadzaca.monopoly.gamelogic.Tile
import com.jacadzaca.monopoly.gamelogic.Transformation
import com.jacadzaca.monopoly.gamelogic.estates.Estate
import com.jacadzaca.monopoly.gamelogic.estates.EstateFactory
import com.jacadzaca.monopoly.gamelogic.estates.EstateType
import java.math.BigInteger
import java.util.*

data class EstatePurchase(
  private val buyer: Player,
  private val buyersId: UUID,
  private val tile: Tile,
  private val tileIndex: Int,
  private val estateType: EstateType,
  private val create: (EstateType) -> Estate,
  private val priceOf: (EstateType) -> BigInteger
) : Transformation {
  companion object {
    fun create(buyer: Player, buyersId: UUID, tile: Tile, tileIndex: Int, estateType: EstateType): EstatePurchase {
      val estateFactory = EstateFactory.standardEstateFactory
      return EstatePurchase(buyer, buyersId, tile, tileIndex, estateType, estateFactory::create, estateFactory::getPriceFor)
    }
  }

  override fun apply(target: GameState): GameState {
    return target
      .update(tileIndex, tile.addEstate(create(estateType)))
      .update(buyersId, buyer.detractFunds(priceOf(estateType)))
  }
}
