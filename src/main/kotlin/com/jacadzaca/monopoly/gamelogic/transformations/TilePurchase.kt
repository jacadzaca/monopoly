package com.jacadzaca.monopoly.gamelogic.transformations

import com.jacadzaca.monopoly.gamelogic.GameState
import com.jacadzaca.monopoly.gamelogic.Player
import com.jacadzaca.monopoly.gamelogic.Tile
import java.util.*

class TilePurchase(
  private val buyer: Player,
  private val buyersId: UUID,
  private val tile: Tile,
  private val tileIndex: Int,
  private val target: GameState
) : Transformation() {
  override fun transform(): GameState {
    return target
      .update(tileIndex, tile.changeOwner(buyersId))
      .update(buyersId, buyer.detractFunds(tile.price))
  }
}
