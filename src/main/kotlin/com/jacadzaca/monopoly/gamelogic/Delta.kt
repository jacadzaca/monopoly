@file:UseSerializers(BigIntegerSerializer::class, UUIDSerializer::class)
package com.jacadzaca.monopoly.gamelogic

import com.jacadzaca.monopoly.serializers.*
import kotlinx.serialization.*
import java.math.*
import java.util.*

@Serializable
sealed class Delta {
  abstract fun apply(gameState: GameState): GameState
  @Serializable
  @SerialName("positionChange")
  data class PositionChange(val playersId: UUID, val newPosition: Int): Delta() {
    override fun apply(gameState: GameState): GameState {
      return gameState.put(playersId, gameState.players[playersId]!!.setPosition(newPosition))
    }
  }

  @Serializable
  @SerialName("balanceChange")
  data class BalanceChange(val playersId: UUID, val newBalance: BigInteger): Delta() {
    override fun apply(gameState: GameState): GameState {
      return gameState.put(playersId, gameState.players[playersId]!!.setBalance(newBalance))
    }
  }

  @Serializable
  @SerialName("tileOwnershipChange")
  data class TileOwnershipChange(val newOwner: UUID, val tilesIndex: Int): Delta() {
    override fun apply(gameState: GameState): GameState {
      return gameState.updateTile(tilesIndex, newOwner = newOwner)
    }
  }

  @Serializable
  @SerialName("estateAdded")
  data class EstateAddition(val tilesIndex: Int, val estate: Estate): Delta() {
    override fun apply(gameState: GameState): GameState {
      return gameState.updateTile(tilesIndex, newEstate = estate)
    }
  }

  @Serializable
  @SerialName("playerJoin")
  data class PlayerJoin(val playersId: UUID, val newPlayer: Player): Delta() {
    override fun apply(gameState: GameState): GameState {
      return gameState.put(playersId, newPlayer)
    }
  }

  @Serializable
  @SerialName("playerLeave")
  data class PlayerLeave(val playersId: UUID, val reason: String): Delta() {
    override fun apply(gameState: GameState): GameState {
      return gameState.remove(playersId, reason)
    }
  }

  @Serializable
  @SerialName("turnChange")
  data class TurnChange(val turn: Int): Delta() {
    override fun apply(gameState: GameState): GameState {
      return gameState.copy(currentTurn = turn)
    }
  }

  @Serializable
  @SerialName("nameChange")
  data class NameChange(val playersId: UUID, val name: String): Delta() {
      override fun apply(gameState: GameState): GameState {
        return gameState.updatePlayer(playersId, newName = name)
      }
  }
}

