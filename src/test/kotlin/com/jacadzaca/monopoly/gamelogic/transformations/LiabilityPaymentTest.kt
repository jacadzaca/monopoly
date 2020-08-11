package com.jacadzaca.monopoly.gamelogic.transformations

import com.jacadzaca.monopoly.gamelogic.GameState
import com.jacadzaca.monopoly.gamelogic.Liability
import com.jacadzaca.monopoly.gamelogic.Player
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.math.BigInteger
import java.util.*
import kotlin.random.Random

internal class LiabilityPaymentTest {
  private val payer = createPlayer()
  private val receiver = createPlayer()
  private val liability = mockk<Liability>()
  private val gameState = mockk<GameState>()
  private val payersId = UUID.randomUUID()
  private val transformation = LiabilityPayment(payer, payersId, liability, gameState)

  @BeforeEach
  fun setUp() {
    every { liability.recevier } returns receiver
    every { liability.recevierId } returns UUID.randomUUID()
    every { liability.amount } returns payer.balance - BigInteger.ONE
    val payerSlot = slot<Player>()
    val recevierSlot = slot<Player>()
    every { gameState.update(payersId, capture(payerSlot)) } answers {
      every { gameState.players[payersId] } returns payerSlot.captured
      gameState
    }
    every { gameState.update(liability.recevierId, capture(recevierSlot)) } answers {
      every { gameState.players[liability.recevierId] } returns recevierSlot.captured
      gameState
    }
  }

  @Test
  fun `transform detracts from the payer's balance`() {
    val actual = transformation.transform()
    assertEquals(
      payer.balance - liability.amount,
      actual.players[payersId]!!.balance
    )
  }

  @Test
  fun `transform adds the amount to the receiver's balance`() {
    val actual = transformation.transform()
    assertEquals(
      receiver.balance + liability.amount,
      actual.players[liability.recevierId]!!.balance
    )
  }

  @Test
  fun `transform only transfers what the payer has`() {
    every { liability.amount } returns payer.balance + BigInteger.ONE
    val actual = transformation.transform()
    assertEquals(
      receiver.balance + payer.balance,
      actual.players[liability.recevierId]!!.balance
    )
  }

  private fun createPlayer(): Player = Player(Random.nextInt(), Random.nextInt().toBigInteger())
}
