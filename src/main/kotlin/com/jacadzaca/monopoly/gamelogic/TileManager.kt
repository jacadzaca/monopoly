package com.jacadzaca.monopoly.gamelogic

interface TileManager {
  fun buyTile(buyer: Player, toBuy: Tile): Tile
}
