package com.jacadzaca.monopoly.gamelogic.commands

import com.jacadzaca.monopoly.gamelogic.*
import java.util.*

class BuyTile(
  private val buyer: Player,
  private val buyersId: UUID,
  private val tile: Tile,
  private val tileIndex: Int,
  private val target: GameState
) : Command() {
  override fun asEvent(): Event = Event.TilePurchased(buyersId, tileIndex)

  override fun execute(): GameState {
    return target
      .updateTile(tileIndex, newOwner = buyersId)
      .updatePlayer(buyersId, newBalance = buyer.balance - tile.price)
  }
}
