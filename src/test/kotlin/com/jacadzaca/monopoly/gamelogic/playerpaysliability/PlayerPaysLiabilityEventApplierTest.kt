package com.jacadzaca.monopoly.gamelogic.playerpaysliability

import com.jacadzaca.monopoly.gamelogic.GameState
import com.jacadzaca.monopoly.gamelogic.player.Liability
import com.jacadzaca.monopoly.gamelogic.player.Player
import com.jacadzaca.monopoly.createPlayer
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.math.BigInteger
import java.util.*

internal class PlayerPaysLiabilityEventApplierTest {
  private val payer = createPlayer()
  private val receiver = createPlayer()
  private val liability = mockk<Liability>()
  private val gameState = mockk<GameState>()
  private val eventApplier = PlayerPaysLiabilityEventApplier()
  private val event = PlayerPaysLiabilityEvent(UUID.randomUUID(), payer, liability)

  @BeforeEach
  fun setUp() {
    every { liability.recevier } returns receiver
    every { liability.recevierId } returns UUID.randomUUID()
    every { liability.amount } returns payer.balance / 2.toBigInteger()
    val payerSlot = slot<Player>()
    val recevierSlot = slot<Player>()
    every { gameState.update(event.payerId, capture(payerSlot)) } answers {
      every { gameState.players[event.payerId] } returns payerSlot.captured
      gameState
    }
    every { gameState.update(event.liability.recevierId, capture(recevierSlot)) } answers {
      every { gameState.players[event.liability.recevierId] } returns recevierSlot.captured
      gameState
    }
  }

  @Test
  fun `apply detracts from the payer's balance`() {
    val payerWithDetractedFunds = payer.copy(balance = payer.balance - event.liability.amount)
    val actual = eventApplier.apply(event, gameState)
    assertEquals(
      payerWithDetractedFunds.balance,
      actual.players[event.payerId]!!.balance
    )
  }

  @Test
  fun `apply adds the amount to the receiver's balance`() {
    val receiverWithAddedFunds = receiver.copy(balance = receiver.balance + event.liability.amount)
    val actual = eventApplier.apply(event, gameState)
    assertEquals(
      receiverWithAddedFunds.balance,
      actual.players[event.liability.recevierId]!!.balance
    )
  }

  @Test
  fun `apply only transfers what the payer has`() {
    every { liability.amount } returns payer.balance + BigInteger.ONE
    val receiverWithAddedFunds = receiver.copy(balance = receiver.balance + payer.balance)
    val actual = eventApplier.apply(event, gameState)
    assertEquals(receiverWithAddedFunds.balance, actual.players[event.liability.recevierId]!!.balance)
  }
}
