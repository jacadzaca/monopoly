package com.jacadzaca.monopoly.gamelogic.tiles

import com.jacadzaca.monopoly.gamelogic.buildings.BuildingType
import com.jacadzaca.monopoly.gamelogic.player.Player

interface TileManager {
  fun buyTile(buyer: Player, toBuy: Tile): Tile
  fun buyProperty(buyer: Player, onTile: Tile, buildingType: BuildingType): Pair<Player, Tile>
}
