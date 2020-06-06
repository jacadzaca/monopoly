package com.jacadzaca.monopoly.gamelogic.tiles

import com.jacadzaca.monopoly.gamelogic.buildings.BuildingFactory
import com.jacadzaca.monopoly.gamelogic.buildings.BuildingType
import com.jacadzaca.monopoly.gamelogic.player.Player

internal class TileManagerImpl(private val buildingFactory: BuildingFactory,
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

  override fun buyProperty(buyer: Player, onTile: Tile, buildingType: BuildingType): Pair<Player, Tile> {
    if (onTile.owner != buyer.id) {
      throw IllegalArgumentException("$buyer dose not own the $onTile, he cannot purchase a property")
    }
    if (buyer.balance < buildingFactory.getPriceFor(buildingType)) {
      throw IllegalArgumentException("insufficient funds for $buildingType $onTile for $buyer")
    }
    if (buildingType == BuildingType.HOTEL && onTile.houseCount() != requiredHousesForHotel) {
      throw IllegalArgumentException("In order for $buyer to buy a property on $onTile, they must own " +
        "$requiredHousesForHotel houses")
    }
    return Pair(
      buyer.copy(balance = buyer.balance - buildingFactory.getPriceFor(buildingType)),
      onTile.copy(buildings = onTile.buildings.add(buildingFactory.create(buildingType))))
  }
}
