package com.jacadzaca.monopoly.gamelogic

import com.jacadzaca.monopoly.gamelogic.commands.Command
import com.jacadzaca.monopoly.serializers.GameStateSerializer
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.PersistentMap
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.serialization.Serializable
import java.math.BigInteger
import java.util.UUID

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

    fun updatePlayer(playersId: UUID, newPosition: Int? = null, newBalance: BigInteger? = null, newName: String? = null): GameState {
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
        if (newName != null) {
            player = player.setName(newName)
            changes = changes.add(Delta.NameChange(playersId, newName))
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

    fun remove(playersId: UUID, reason: String): GameState =
        copy(
            players = players.remove(playersId),
            turnOrder = turnOrder.remove(playersId),
            recentChanges = recentChanges.add(Delta.PlayerLeave(playersId, reason))
        )

    fun clearRecentChanges(): GameState = copy(recentChanges = recentChanges.clear())

    fun isPlayersTurn(playersId: UUID): Boolean = turnOrder[currentTurn] == playersId

    fun executeCommand(command: Command): GameState = command.execute()

    fun executeCommandIf(predicate: Boolean, command: Lazy<Command>): GameState {
        return if (predicate) {
            executeCommand(command.value)
        } else {
            this
        }
    }

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
