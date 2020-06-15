package com.jacadzaca.monopoly.gamelogic.tiles

import com.jacadzaca.monopoly.gamelogic.buildings.BuildingType
import com.jacadzaca.monopoly.gamelogic.player.Player

interface TileManager {
  /**
   * This method dose NOT update the buyer's balance, it is left to the caller
   * @throws IllegalArgumentException, if @buyer.balance < @toBuy.price
   * @throws IllegalArgumentException, if @buyer.id == @toBuy.owner
   * @return a copy of @toBuy with owner set to @buyer's id
   */
  fun buyTile(buyer: Player, toBuy: Tile): Tile

  /**
   * This method dose NOT update the buyer's balance, it is left to the caller
   * @throws IllegalArgumentException, if @onTile.owner != @buyer.id
   * @throws IllegalArgumentException, if @buyer.balance < @buildingFactory.getPriceFor(buildingType)
   * @throws IllegalArgumentException, if @buildingType == BuildingType.HOTEL && @onTile.houseCount() != requiredHousesForHotel
   * @return a copy of @onTile with a new building of @buildingType
   */
  fun buyProperty(buyer: Player, onTile: Tile, buildingType: BuildingType): Tile
}
