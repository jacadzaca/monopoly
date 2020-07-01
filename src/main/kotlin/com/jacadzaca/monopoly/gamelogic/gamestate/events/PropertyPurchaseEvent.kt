package com.jacadzaca.monopoly.gamelogic.gamestate.events

import com.jacadzaca.monopoly.gamelogic.buildings.BuildingType
import com.jacadzaca.monopoly.gamelogic.player.PlayerID
import java.util.*

data class PropertyPurchaseEvent(override val playerId: PlayerID,
                                 val propertyType: BuildingType,
                                 val whereToBuy: Int): GameEvent
