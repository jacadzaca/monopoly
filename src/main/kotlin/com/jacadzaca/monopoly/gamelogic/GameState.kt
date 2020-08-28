@file:UseSerializers(
  PlayersMapSerializer::class,
  UUIDSerializer::class,
  PlayersMapSerializer::class,
  TileSerializer::class)
package com.jacadzaca.monopoly.gamelogic

import com.jacadzaca.monopoly.gamelogic.commands.*
import com.jacadzaca.monopoly.serializers.*
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.PersistentMap
import kotlinx.collections.immutable.persistentListOf
import kotlinx.serialization.*
import java.util.*

@Serializable
data class GameState(
  val players: PersistentMap<UUID, Player>,
  val tiles: PersistentList<Tile>,
  val events: PersistentList<Event> = persistentListOf()
) {
  /**
   * @return a copy of this, where the player under @playersId is @updatedPlayer
   */
  fun update(playersId: UUID, updatedPlayer: Player): GameState = copy(players = players.put(playersId, updatedPlayer))

  /**
   * @throws IndexOutOfBoundsException, if no tile will be found at @tileIndex
   * @return a copy of this, where the tile at index @tileIndex is @updatedTile
   */

  fun update(tileIndex: Int, updatedTile: Tile): GameState = copy(tiles = tiles.set(tileIndex, updatedTile))

  fun addEvent(event: Event): GameState = copy(events = events.add(event))
}
