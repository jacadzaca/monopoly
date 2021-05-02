package com.jacadzaca.monopoly.gamelogic.commands

import com.jacadzaca.monopoly.gamelogic.*
import java.math.*
import java.util.*

/**
 * target.boardSize > PlayerMoves.newPosition >= 0
 */
data class MovePlayer(
  private val player: Player,
  private val playersId: UUID,
  private val newPosition: Int,
  private val target: GameState,
  private val onEnterForeignTile: Lazy<Command> = lazy {
      val tile = target.tiles[newPosition]
      PayLiability(player, playersId, target.players[tile.ownersId]!!, tile.ownersId!!, tile.totalRent(), LeavePlayer(playersId, "player bankrupted", target), target)
  },
) : Command {
  override fun execute(): GameState {
    val tile = target.tiles[newPosition]
    return target
        .executeCommandIf(tile.ownersId != null && tile.ownersId != playersId, onEnterForeignTile)
        .updatePlayer(playersId, newPosition = newPosition)
  }
}

