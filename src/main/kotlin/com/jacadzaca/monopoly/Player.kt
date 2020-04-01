package com.jacadzaca.monopoly

interface Player {
  val piece: Piece
  fun copyPlayer(piece: Piece): Player
}
