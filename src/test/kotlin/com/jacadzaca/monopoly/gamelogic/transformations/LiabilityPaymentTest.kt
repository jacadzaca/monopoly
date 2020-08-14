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
  private val receiversId = UUID.randomUUID()
  private val gameState = mockk<GameState>()
  private val payersBalance = randomPositive().toBigInteger()
  private val liability = payersBalance - BigInteger.ONE
  private val transformation = LiabilityPayment(payer, payersId, receiver, receiversId, liability, gameState)

  @BeforeEach
  fun setUp() {
    clearAllMocks()
    every { payer.balance } returns payersBalance
    every { gameState.update(payersId, any()) } returns gameState
    every { gameState.update(receiversId, any()) } returns gameState
  }

  @Test
  fun `transform detracts from the payer's balance`() {
    transformation.transform()
    verify { gameState.update(payersId, payer.detractFunds(liability)) }
  }

  @Test
  fun `transform adds the amount to the receiver's balance`() {
    transformation.transform()
    verify { gameState.update(receiversId, receiver.addFunds(liability)) }
  }

  @Test
  fun `transform only transfers what the payer has`() {
    val liability = payer.balance + BigInteger.ONE
    val transformation = LiabilityPayment(payer, payersId, receiver, receiversId, liability, gameState)
    transformation.transform()
    verify { gameState.update(receiversId, receiver.addFunds(payersBalance)) }
    verify { gameState.update(payersId, payer.detractFunds(liability)) }
  }
}
