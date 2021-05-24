package com.jacadzaca.monopoly.requests

import com.jacadzaca.monopoly.Computation
import com.jacadzaca.monopoly.gamelogic.GameState
import com.jacadzaca.monopoly.gamelogic.commands.Command

interface ValidatorProxy {
    fun validate(request: Request, context: GameState): Computation<Command>
}
