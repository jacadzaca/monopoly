package com.jacadzaca.monopoly.gamelogic

import com.jacadzaca.monopoly.serializers.*
import kotlinx.collections.immutable.*
import kotlinx.serialization.*
import java.math.*
import java.util.*

@Serializable(with = GameStateSerializer::class)
data class GameState(
  val players: PersistentMap<UUID, Player>,
  val tiles: PersistentList<Tile>,
  val currentTurn: Int = 0,
  val turnOrder: PersistentList<UUID> = players.keys.toPersistentList(),
  val recentChanges: PersistentList<Delta> = persistentListOf(),
) {
  fun put(playersId: UUID, newPlayer: Player): GameState {
    return copy(
      players = players.put(playersId, newPlayer),
      recentChanges = recentChanges.add(Delta.PlayerJoin(playersId, newPlayer)),
      turnOrder = turnOrder.add(playersId)
    )
  }

  fun updatePlayer(playersId: UUID, newPosition: Int? = null, newBalance: BigInteger? = null): GameState {
    var player = players[playersId]!!
    var changes = recentChanges
    if (newPosition != null) {
      player = player.setPosition(newPosition)
      changes = changes.add(Delta.PositionChange(playersId, newPosition))
    }
    if (newBalance != null) {
      player = player.setBalance(newBalance)
      changes = changes.add(Delta.BalanceChange(playersId, newBalance))
    }
    return copy(players = players.put(playersId, player), recentChanges = changes)
  }

  fun updateTile(tileIndex: Int, newOwner: UUID? = null, newEstate: Estate? = null): GameState {
    var tile = tiles[tileIndex]
    var changes = recentChanges
    if (newOwner != null) {
      tile = tile.changeOwner(newOwner)
      changes = changes.add(Delta.TileOwnershipChange(newOwner, tileIndex))
    }
    if (newEstate != null) {
      tile = tile.addEstate(newEstate)
      changes = changes.add(Delta.EstateAddition(tileIndex, newEstate))
    }
    return copy(tiles = tiles.set(tileIndex, tile), recentChanges = changes)
  }

  fun updateTurn(newTurn: Int): GameState =
    copy(currentTurn = newTurn, recentChanges = recentChanges.add(Delta.TurnChange(newTurn)))


  fun remove(playersId: UUID): GameState =
    copy(
      players = players.remove(playersId),
      turnOrder = turnOrder.remove(playersId),
      recentChanges = recentChanges.add(Delta.PlayerLeave(playersId))
    )

  fun clearRecentChanges(): GameState = copy(recentChanges = recentChanges.clear())

  fun isPlayersTurn(playersId: UUID): Boolean = turnOrder[currentTurn] == playersId

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

