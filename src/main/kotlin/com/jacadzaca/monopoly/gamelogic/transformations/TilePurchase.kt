package com.jacadzaca.monopoly.gamelogic.transformations

import com.jacadzaca.monopoly.gamelogic.GameState
import com.jacadzaca.monopoly.gamelogic.Player
import com.jacadzaca.monopoly.gamelogic.Tile
import java.util.*

data class TilePurchase(
  val buyer: Player,
  val buyersId: UUID,
  val tile: Tile,
  val tileIndex: Int,
  private val target: GameState
) : Transformation() {
  override fun transform(): GameState {
    return target
      .update(tileIndex, tile.changeOwner(buyersId))
      .update(buyersId, buyer.detractFunds(tile.price))
  }
}