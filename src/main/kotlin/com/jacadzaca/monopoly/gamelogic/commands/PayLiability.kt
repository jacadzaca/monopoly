package com.jacadzaca.monopoly.gamelogic.commands

import com.jacadzaca.monopoly.gamelogic.GameState
import com.jacadzaca.monopoly.gamelogic.Player
import java.math.BigInteger
import java.util.UUID

class PayLiability(
    private val payer: Player,
    private val payerId: UUID,
    private val receiver: Player,
    private val receiversId: UUID,
    private val liability: BigInteger,
    private val onBankrupcy: Command,
    private val target: GameState,
) : Command {
    override fun execute(): GameState {
        return if (liability > payer.balance) {
            target
                .updatePlayer(payerId, newBalance = BigInteger.ZERO)
                .updatePlayer(receiversId, newBalance = receiver.balance + payer.balance)
                .executeCommand(onBankrupcy)
        } else {
            target
                .updatePlayer(payerId, newBalance = payer.balance - liability)
                .updatePlayer(receiversId, newBalance = receiver.balance + liability)
        }
    }
}
