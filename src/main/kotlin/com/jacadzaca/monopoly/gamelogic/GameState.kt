package com.jacadzaca.monopoly.gamelogic

import com.jacadzaca.monopoly.serializers.*
import kotlinx.collections.immutable.*
import kotlinx.serialization.*
import java.util.*

@Serializable(with = GameStateSerializer::class)
data class GameState(
  // must be an implementation that preserves the order of insertion
  val players: PersistentMap<UUID, Player>,
  val tiles: PersistentList<Tile>,
) {
  /**
   * @return a copy of this, where the player under @playersId is @updatedPlayer
   */
  fun put(playersId: UUID, updatedPlayer: Player): GameState = copy(players = players.put(playersId, updatedPlayer))

  /**
   * @throws IndexOutOfBoundsException, if no tile will be found at @tileIndex
   * @return a copy of this, where the tile at index @tileIndex is @updatedTile
   */

  fun put(tileIndex: Int, updatedTile: Tile): GameState = copy(tiles = tiles.set(tileIndex, updatedTile))
}
