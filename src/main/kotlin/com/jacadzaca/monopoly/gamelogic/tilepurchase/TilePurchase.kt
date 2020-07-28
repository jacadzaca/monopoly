package com.jacadzaca.monopoly.gamelogic.tilepurchase

import com.jacadzaca.monopoly.gamelogic.GameState
import com.jacadzaca.monopoly.gamelogic.Player
import com.jacadzaca.monopoly.gamelogic.Tile
import com.jacadzaca.monopoly.gamelogic.Transformation
import java.util.*

data class TilePurchase(
  val buyer: Player,
  val buyersId: UUID,
  val tile: Tile,
  val tileIndex: Int
) : Transformation {
  override fun apply(target: GameState): GameState {
    return target
      .update(tileIndex, tile.changeOwner(buyersId))
      .update(buyersId, buyer.detractFunds(tile.price))
  }
}
