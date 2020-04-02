package com.jacadzaca.monopoly

data class TestPlayer(
  override val piece: Piece = Piece()
  ) : Player {
  override fun copyPlayer(piece: Piece): Player {
    return copy(piece = piece)
  }
}
