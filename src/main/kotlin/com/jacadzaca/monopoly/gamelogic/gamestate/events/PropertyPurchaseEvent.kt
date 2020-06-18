package com.jacadzaca.monopoly.gamelogic.gamestate.events

import com.jacadzaca.monopoly.gamelogic.buildings.BuildingType
import java.util.*

data class PropertyPurchaseEvent(override val playerId: UUID,
                                 val propertyType: BuildingType,
                                 val whereToBuy: Int): GameEvent
