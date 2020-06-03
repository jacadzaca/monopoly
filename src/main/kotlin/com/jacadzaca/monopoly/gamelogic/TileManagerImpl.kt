package com.jacadzaca.monopoly.gamelogic

internal class TileManagerImpl : TileManager {
  override fun buyTile(buyer: Player, toBuy: Tile): Tile {
    if (buyer.balance < toBuy.price) {
      throw IllegalArgumentException("Insufficient funds to buy $toBuy by $buyer")
    }
    if (buyer.id == toBuy.owner) {
      throw IllegalArgumentException("$buyer cannot buy a tile that is already owned by him")
    }
    return toBuy.copy(owner = buyer.id)
  }
}
