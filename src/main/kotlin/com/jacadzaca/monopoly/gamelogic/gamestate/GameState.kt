package com.jacadzaca.monopoly.gamelogic.gamestate

import com.jacadzaca.monopoly.gamelogic.player.Player
import com.jacadzaca.monopoly.gamelogic.player.PlayerID
import com.jacadzaca.monopoly.gamelogic.tiles.Tile
import java.util.*

interface GameState {
  val boardSize: Int
  /**
   * @return a copy of this, where the player under @playersId is @updatedPlayer
   */

  fun update(playersId: PlayerID, updatedPlayer: Player): GameState
  /**
   * @throws IndexOutOfBoundsException, if no tile will be found at @tileIndex
   * @return a copy of this, where the tile at index @tileIndex is @updatedTile
   */
  fun update(tileIndex: Int, updatedTile: Tile): GameState

  /**
   * @throws NoSuchElementException, if player with id @playerId is not present in the game
   */
  fun getPlayer(playersId: PlayerID): Player

  /**
   * @throws IndexOutOfBoundsException, if no tile will be found at @tileIndex
   */
  fun getTile(tileIndex: Int): Tile
}
