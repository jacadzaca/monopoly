package com.jacadzaca.monopoly.gamelogic.gamestate

import com.jacadzaca.monopoly.gamelogic.player.Player
import com.jacadzaca.monopoly.gamelogic.player.PlayerID
import com.jacadzaca.monopoly.gamelogic.Tile
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.PersistentMap
import kotlinx.collections.immutable.persistentListOf

data class GameState(
    val players: PersistentMap<PlayerID, Player>,
    val tiles: PersistentList<Tile>,
    val recentChanges: PersistentList<GameStateChange> = persistentListOf()
) {
   val boardSize: Int
    get() = tiles.size

  /**
   * @return a copy of this, where the player under @playersId is @updatedPlayer
   */
   fun update(playersId: PlayerID, updatedPlayer: Player): GameState =
    copy(players = players.put(playersId, updatedPlayer))

  /**
   * @throws IndexOutOfBoundsException, if no tile will be found at @tileIndex
   * @return a copy of this, where the tile at index @tileIndex is @updatedTile
   */

   fun update(tileIndex: Int, updatedTile: Tile): GameState =
    copy(tiles = tiles.set(tileIndex, updatedTile))

  fun addChange(event: GameStateChange): GameState =
    copy(recentChanges = recentChanges.add(event))
}
