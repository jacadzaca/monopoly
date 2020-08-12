package com.jacadzaca.monopoly.requests

import java.util.*

interface RequestFactory {
  fun playerMoveRequest(playersId: UUID, gameStatesId: UUID): Request
  fun tilePurchaseRequest(playersId: UUID, gameStatesId: UUID): Request
  fun housePurchaseRequest(buyersId: UUID, gameStatesId: UUID): Request
  fun hotelPurchaseRequest(buyersId: UUID, gameStatesId: UUID): Request
}
