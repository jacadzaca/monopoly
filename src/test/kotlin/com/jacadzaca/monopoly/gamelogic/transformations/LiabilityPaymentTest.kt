package com.jacadzaca.monopoly.gamelogic.transformations

import com.jacadzaca.monopoly.*
import com.jacadzaca.monopoly.gamelogic.*
import io.mockk.*
import org.junit.jupiter.api.*
import java.math.*
import java.util.*

internal class LiabilityPaymentTest {
  private val payer = mockk<Player>(relaxed = true)
  private val receiver = mockk<Player>(relaxed = true)
  private val payersId = UUID.randomUUID()
  private val liability = mockk<Liability>()
  private val gameState = mockk<GameState>()
  private val transformation = LiabilityPayment(payer, payersId, liability, gameState)

  @BeforeEach
  fun setUp() {
    every { payer.balance } returns randomPositive().toBigInteger()
    every { liability.recevier } returns receiver
    every { liability.recevierId } returns UUID.randomUUID()
    every { liability.amount } returns payer.balance - BigInteger.ONE
    every { gameState.update(payersId, any()) } returns gameState
    every { gameState.update(liability.recevierId, any()) } returns gameState
  }

  @Test
  fun `transform detracts from the payer's balance`() {
    transformation.transform()
    val amount = liability.amount
    verify { payer.detractFunds(amount) }
  }

  @Test
  fun `transform adds the amount to the receiver's balance`() {
    transformation.transform()
    val amount = liability.amount
    verify { receiver.addFunds(amount) }
  }

  @Test
  fun `transform only transfers what the payer has`() {
    every { liability.amount } returns payer.balance + BigInteger.ONE
    transformation.transform()
    val amount = liability.amount
    val payersBalance = payer.balance
    verify { receiver.addFunds(payersBalance) }
    verify { payer.detractFunds(amount) }
  }
}
