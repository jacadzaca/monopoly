package com.jacadzaca.monopoly.gamelogic.tiles

import com.jacadzaca.monopoly.gamelogic.estates.EstateFactory
import com.jacadzaca.monopoly.gamelogic.estates.EstateType
import com.jacadzaca.monopoly.gamelogic.player.Player

internal class TileManagerImpl(private val estateFactory: EstateFactory,
                               private val requiredHousesForHotel: Int)
  : TileManager {
  override fun buyTile(buyer: Player, toBuy: Tile): Tile {
    if (buyer.balance < toBuy.price) {
      throw IllegalArgumentException("Insufficient funds to buy $toBuy by $buyer")
    }
    if (buyer.id == toBuy.owner) {
      throw IllegalArgumentException("$buyer cannot buy a tile that is already owned by him")
    }
    return toBuy.copy(owner = buyer.id)
  }

  override fun buyProperty(buyer: Player, onTile: Tile, estateType: EstateType): Tile {
    if (onTile.owner != buyer.id) {
      throw IllegalArgumentException("$buyer dose not own the $onTile, he cannot purchase a property")
    }
    if (buyer.balance < estateFactory.getPriceFor(estateType)) {
      throw IllegalArgumentException("insufficient funds for $estateType $onTile for $buyer")
    }
    if (estateType == EstateType.HOTEL && onTile.houseCount() != requiredHousesForHotel) {
      throw IllegalArgumentException("In order for $buyer to buy a property on $onTile, they must own " +
        "$requiredHousesForHotel houses")
    }
    return onTile.copy(estates = onTile.estates.add(estateFactory.create(estateType)))
  }
}
