package com.jacadzaca.monopoly.gamelogic.transformations

import com.jacadzaca.monopoly.gamelogic.GameState
import com.jacadzaca.monopoly.gamelogic.Player
import com.jacadzaca.monopoly.gamelogic.Tile
import com.jacadzaca.monopoly.gamelogic.Estate
import java.util.*

data class EstatePurchase(
  private val buyer: Player,
  private val buyersId: UUID,
  private val tile: Tile,
  private val tileIndex: Int,
  private val estate: Estate,
  private val target: GameState
) : Transformation {
  override fun transform(): GameState {
    return target
      .update(tileIndex, tile.addEstate(estate))
      .update(buyersId, buyer.detractFunds(estate.price))
  }
}
