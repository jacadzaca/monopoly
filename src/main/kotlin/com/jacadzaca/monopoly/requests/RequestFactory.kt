package com.jacadzaca.monopoly.requests

import java.util.*

interface RequestFactory {
  fun playerMoveRequest(playersId: UUID, gameStatesId: UUID): PlayerMovementRequest
  fun tilePurchaseRequest(playersId: UUID, gameStatesId: UUID): TilePurchaseRequest
  fun housePurchaseRequest(buyersId: UUID, gameStatesId: UUID): EstatePurchaseRequest
  fun hotelPurchaseRequest(buyersId: UUID, gameStatesId: UUID): EstatePurchaseRequest
}
