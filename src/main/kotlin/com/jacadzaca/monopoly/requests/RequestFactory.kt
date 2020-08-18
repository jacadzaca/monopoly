package com.jacadzaca.monopoly.requests

import com.jacadzaca.monopoly.gamelogic.*
import java.util.*

interface RequestFactory {
  fun playerMoveRequest(playersId: UUID, gameState: GameState): PlayerMovementRequest
  fun tilePurchaseRequest(playersId: UUID, gameState: GameState): TilePurchaseRequest
  fun housePurchaseRequest(playersId: UUID, gameState: GameState): EstatePurchaseRequest
  fun hotelPurchaseRequest(playersId: UUID, gameState: GameState): EstatePurchaseRequest
}
