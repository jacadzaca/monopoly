@file:UseSerializers(GameStateSerializer::class)

package com.jacadzaca.monopoly.gameroom

import com.jacadzaca.monopoly.gamelogic.*
import com.jacadzaca.monopoly.serializers.*
import io.vertx.core.buffer.*
import io.vertx.core.shareddata.impl.*
import kotlinx.serialization.*
import kotlinx.serialization.json.*

@Serializable
/**
 * To create an instance, use the (gameState, version) constructor
 * The no-argument constructor is to provide compliance with the [ClusterSerializable] contract
 */
class GameRoom() : ClusterSerializable {
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

  fun incrementVersion(): GameRoom {
    return GameRoom(_gameState, _version + 1L)
  }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as GameRoom

    if (_gameState != other._gameState) return false
    if (_version != other._version) return false

    return true
  }

  override fun hashCode(): Int {
    var result = _gameState.hashCode()
    result = 31 * result + _version.hashCode()
    return result
  }
}
