package com.jacadzaca.monopoly.gamelogic.player

interface PlayerMover {
  fun move(toMove: Player, boardSize: Int): Player
}
