package com.jacadzaca.monopoly.gamelogic

import com.jacadzaca.monopoly.gamelogic.commands.*
import com.jacadzaca.monopoly.serializers.*
import kotlinx.collections.immutable.*
import kotlinx.serialization.*
import java.util.*

@Serializable(with = GameStateSerializer::class)
data class GameState(
  val players: PersistentMap<UUID, Player>,
  val tiles: PersistentList<Tile>,
  val currentTurn: Int = 0,
  val turnOrder: PersistentList<UUID> = players.keys.toPersistentList(),
  val recentEvents: PersistentList<Event> =  persistentListOf(),
) {
  fun put(playersId: UUID, updatedPlayer: Player): GameState = copy(players = players.put(playersId, updatedPlayer))

  fun put(tileIndex: Int, updatedTile: Tile): GameState = copy(tiles = tiles.set(tileIndex, updatedTile))

  fun remove(playersId: UUID): GameState =
    copy(players = players.remove(playersId), turnOrder = turnOrder.remove(playersId))

  fun addPlayerToTurnOrder(playersId: UUID): GameState = copy(turnOrder = turnOrder.add(playersId))

  fun isPlayersTurn(playersId: UUID): Boolean = turnOrder[currentTurn] == playersId

  fun addRecentEvent(event: Event): GameState = copy(recentEvents = recentEvents.add(event))

  fun clearRecentEvent(): GameState = copy(recentEvents = recentEvents.clear())

  fun disownPlayer(playersId: UUID): GameState {
    return copy(
      tiles = tiles.map { tile ->
        if (tile.ownersId == playersId) {
          tile.changeOwner(null)
        } else {
          tile
        }
      }.toPersistentList()
    )
  }
}
