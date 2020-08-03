package com.jacadzaca.monopoly.gamelogic

import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.PersistentMap
import kotlinx.collections.immutable.persistentListOf
import java.util.*

data class GameState(
  val players: PersistentMap<UUID, Player>,
  val tiles: PersistentList<Tile>,
  val recentChanges: PersistentList<Transformation> = persistentListOf()
) {
   val boardSize: Int
    get() = tiles.size

  /**
   * @return a copy of this, where the player under @playersId is @updatedPlayer
   */
   fun update(playersId: UUID, updatedPlayer: Player): GameState =
    copy(players = players.put(playersId, updatedPlayer))

  /**
   * @throws IndexOutOfBoundsException, if no tile will be found at @tileIndex
   * @return a copy of this, where the tile at index @tileIndex is @updatedTile
   */

   fun update(tileIndex: Int, updatedTile: Tile): GameState =
    copy(tiles = tiles.set(tileIndex, updatedTile))

  fun addChange(transformation: Transformation): GameState =
    copy(recentChanges = recentChanges.add(transformation))
}
