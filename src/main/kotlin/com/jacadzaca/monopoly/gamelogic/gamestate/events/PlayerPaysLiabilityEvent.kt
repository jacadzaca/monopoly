package com.jacadzaca.monopoly.gamelogic.gamestate.events

import com.jacadzaca.monopoly.gamelogic.player.Liability
import com.jacadzaca.monopoly.gamelogic.player.PlayerID

data class PlayerPaysLiabilityEvent(override val playerId: PlayerID,
                                    val liability: Liability): GameEvent
