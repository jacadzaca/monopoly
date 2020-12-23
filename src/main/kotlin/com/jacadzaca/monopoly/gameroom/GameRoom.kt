@file:UseSerializers(GameStateSerializer::class)

package com.jacadzaca.monopoly.gameroom

import com.jacadzaca.monopoly.gamelogic.*
import com.jacadzaca.monopoly.gamelogic.commands.*
import com.jacadzaca.monopoly.serializers.*
import io.vertx.core.buffer.*
import io.vertx.core.shareddata.impl.*
import kotlinx.collections.immutable.*
import kotlinx.serialization.*
import kotlinx.serialization.json.*

@Serializable
/**
 * To acquire an instance, use GameRoom.CLEAN_GAME_ROOM
 * The no-argument constructor is to provide compliance with the [ClusterSerializable] contract
 */
class GameRoom() : ClusterSerializable {
  companion object {
    private val tile = Tile(persistentListOf(), persistentListOf(), 1000.toBigInteger(), null)
    val CLEAN_GAME_ROOM = GameRoom(GameState(persistentHashMapOf(), persistentListOf(tile, tile, tile, tile, tile)))
  }
  private lateinit var _gameState: GameState
  private var _version: Long = 0L

  val gameState: GameState
    get() = _gameState

  val version: Long
    get() = _version

  constructor(gameState: GameState, version: Long = 0L) : this() {
    this._gameState = gameState
    this._version = version
  }

  override fun readFromBuffer(pos: Int, buffer: Buffer): Int {
    val room = Json.decodeFromString<GameRoom>(buffer.getString(pos, buffer.length()))
    this._gameState = room.gameState
    this._version = room.version
    // why do I even return this? Shouldn't I return [pos + buffer.length()]?
    return pos
  }

  override fun writeToBuffer(buffer: Buffer) {
    buffer.appendString(Json.encodeToString(this))
  }

  fun incrementVersion(): GameRoom = GameRoom(_gameState, _version + 1L)

  fun updateGameState(command: Command): GameRoom = GameRoom(command.apply(), _version)

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as GameRoom

    if (_version != other._version) return false

    return true
  }

  override fun hashCode(): Int {
    return _version.hashCode()
  }


}
