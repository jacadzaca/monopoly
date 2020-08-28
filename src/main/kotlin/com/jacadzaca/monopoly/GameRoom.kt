@file:UseSerializers(GameStateSerializer::class)

package com.jacadzaca.monopoly

import com.jacadzaca.monopoly.gamelogic.*
import com.jacadzaca.monopoly.serializers.*
import io.vertx.core.buffer.*
import io.vertx.core.shareddata.impl.*
import kotlinx.serialization.*
import kotlinx.serialization.json.*

/**
 * To create an instance use the constructor with two arguments [constructor(gameState: GameState, version: Long)].
 * The zero-argument constructor is defined to provide compliance with the [ClusterSerializable] interface
 */
@Serializable
class GameRoom constructor() : ClusterSerializable {
  private lateinit var _gameState: GameState
  private var _version: Long = 0L

  constructor(gameState: GameState, version: Long = 0L) {
    _gameState = gameState
    _version = version
  }

  val gameState
    get() = _gameState
  val version
    get() = _version

  override fun readFromBuffer(pos: Int, buffer: Buffer): Int {
    val gameRoom = Json.decodeFromString<GameRoom>(buffer.getString(pos, buffer.length()))
    this._gameState = gameRoom._gameState
    this._version = gameRoom._version
    // no clue why it is required to return this. Shouldn't I change the return statement to [pos + buffer.length()]?
    return pos
  }

  override fun writeToBuffer(buffer: Buffer) {
    buffer.appendString(Json.encodeToString(this))
  }


}
