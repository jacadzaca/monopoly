package com.jacadzaca.monopoly.gamelogic.commands

import com.jacadzaca.monopoly.gamelogic.GameState
import com.jacadzaca.monopoly.gamelogic.Player
import com.jacadzaca.monopoly.gamelogic.Tile
import java.util.*

class BuyTile(
  private val buyer: Player,
  private val buyersId: UUID,
  private val tile: Tile,
  private val tileIndex: Int,
  private val target: GameState
) : Command() {
  override fun execute(): GameState {
    return target
      .update(tileIndex, tile.changeOwner(buyersId))
      .update(buyersId, buyer.detractFunds(tile.price))
  }
}
