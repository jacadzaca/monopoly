package com.jacadzaca.monopoly.gamelogic.gamestate.events

import com.jacadzaca.monopoly.gamelogic.player.Liability
import java.util.*

data class PlayerPaysLiabilityEvent(override val playerId: UUID,
                                    val liability: Liability): GameEvent
