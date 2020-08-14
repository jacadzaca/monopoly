package com.jacadzaca.monopoly.gamelogic

import com.jacadzaca.monopoly.gamelogic.transformations.Command
import java.util.*
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.PersistentMap
import kotlinx.collections.immutable.persistentListOf

data class GameState(
  val players: PersistentMap<UUID, Player>,
  val tiles: PersistentList<Tile>,
  val commands: PersistentList<Command> = persistentListOf()
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

  fun addTransformation(command: Command): GameState =
    copy(commands = commands.add(command))
}
