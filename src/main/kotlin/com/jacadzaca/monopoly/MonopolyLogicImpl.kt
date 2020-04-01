package com.jacadzaca.monopoly

class MonopolyLogicImpl : MonopolyLogic {
  override fun movePiece(tilesCount: Int, piece: Piece): Piece {
    return piece.copy(position = piece.position + tilesCount)
  }
}
