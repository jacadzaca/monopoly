package com.jacadzaca.monopoly.gamelogic

import com.jacadzaca.monopoly.gamelogic.player.Player

interface TileManager {
  fun buyTile(buyer: Player, toBuy: Tile): Tile
}
