package com.jacadzaca.monopoly.gamelogic.gamestate

import com.jacadzaca.monopoly.gamelogic.player.Player
import com.jacadzaca.monopoly.gamelogic.player.PlayerID
import com.jacadzaca.monopoly.gamelogic.tiles.Tile
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.PersistentMap
import java.util.*

internal data class InMemoryGameState(val players: PersistentMap<PlayerID, Player>,
                                      val tiles: PersistentList<Tile>)
  : GameState {
  override val boardSize: Int
    get() = tiles.size

  override fun update(playersId: PlayerID, updatedPlayer: Player): GameState =
    copy(players = players.put(playersId, updatedPlayer))


  override fun update(tileIndex: Int, updatedTile: Tile): GameState =
      copy(tiles = tiles.set(tileIndex, updatedTile))

  override fun getPlayer(playersId: PlayerID): Player = players.getValue(playersId)
  override fun getTile(tileIndex: Int): Tile = tiles[tileIndex]
}
