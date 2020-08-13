package com.jacadzaca.monopoly.requests

import java.util.*

interface RequestFactory {
  fun playerMoveRequest(playersId: UUID, gameRoomsId: UUID): PlayerMovementRequest
  fun tilePurchaseRequest(playersId: UUID, gameRoomsId: UUID): TilePurchaseRequest
  fun housePurchaseRequest(buyersId: UUID, gameRoomsId: UUID): EstatePurchaseRequest
  fun hotelPurchaseRequest(buyersId: UUID, gameRoomsId: UUID): EstatePurchaseRequest
}
