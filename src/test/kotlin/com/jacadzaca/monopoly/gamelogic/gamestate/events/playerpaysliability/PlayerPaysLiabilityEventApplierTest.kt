package com.jacadzaca.monopoly.gamelogic.gamestate.events.playerpaysliability

import com.jacadzaca.monopoly.gamelogic.gamestate.GameState
import com.jacadzaca.monopoly.gamelogic.player.Liability
import com.jacadzaca.monopoly.gamelogic.player.Player
import com.jacadzaca.monopoly.getTestPlayer
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.math.BigInteger

internal class PlayerPaysLiabilityEventApplierTest {
  private val payer = getTestPlayer()
  private val receiver = getTestPlayer()
  private val liability = mockk<Liability>()
  private val gameState = mockk<GameState>()
  private val eventApplier = PlayerPaysLiabilityEventApplier()
  private val event = PlayerPaysLiabilityEvent(payer.id, payer, liability)

  @BeforeEach
  fun setUp() {
    val payerSlot = slot<Player>()
    val recevierSlot = slot<Player>()
    every { gameState.update(payer.id, capture(payerSlot)) } answers {
      every { gameState.players[payer.id] } returns payerSlot.captured
      gameState
    }
    every { gameState.update(receiver.id, capture(recevierSlot)) } answers {
      every { gameState.players[receiver.id] } returns recevierSlot.captured
      gameState
    }
    every { liability.recevier } returns receiver
    every { liability.recevierId } returns receiver.id
    every { liability.howMuch } returns payer.balance / 2.toBigInteger()
  }

  @Test
  fun `apply detracts from the payer's balance`() {
    val payerWithDetractedFunds = payer.copy(balance = payer.balance - event.liability.howMuch)
    val actual = eventApplier.apply(event, gameState)
    assertEquals(
      payerWithDetractedFunds.balance,
      actual.players[event.playerId]!!.balance
    )
  }

  @Test
  fun `apply adds the amount to the receiver's balance`() {
    val receiverWithAddedFunds = receiver.copy(balance = receiver.balance + event.liability.howMuch)
    val actual = eventApplier.apply(event, gameState)
    assertEquals(
      receiverWithAddedFunds.balance,
      actual.players[event.liability.recevierId]!!.balance
    )
  }

  @Test
  fun `apply only transfers what the payer has`() {
    every { liability.howMuch } returns payer.balance + BigInteger.ONE
    val receiverWithAddedFunds = receiver.copy(balance = receiver.balance + payer.balance)
    val actual = eventApplier.apply(event, gameState)
    assertEquals(receiverWithAddedFunds.balance, actual.players[event.liability.recevierId]!!.balance)
  }
}
