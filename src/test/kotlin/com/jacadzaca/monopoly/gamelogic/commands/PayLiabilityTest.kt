package com.jacadzaca.monopoly.gamelogic.commands

import com.jacadzaca.monopoly.*
import com.jacadzaca.monopoly.gamelogic.*
import io.mockk.*
import org.junit.jupiter.api.*
import java.math.*
import java.util.*
import kotlin.random.Random

internal class PayLiabilityTest {
  private val payer = mockk<Player>(relaxed = true)
  private val receiver = mockk<Player>(relaxed = true)
  private val payersId = UUID.randomUUID()
  private val receiversId = UUID.randomUUID()
  private val gameState = mockk<GameState>()
  private val payersBalance = Random.nextPositive().toBigInteger()
  private val liability = payersBalance - BigInteger.ONE
  private val transformation = PayLiability(payer, payersId, receiver, receiversId, liability, gameState)

  @BeforeEach
  fun setUp() {
    clearAllMocks()
    every { payer.balance } returns payersBalance
    every { gameState.put(payersId, any()) } returns gameState
    every { gameState.put(receiversId, any()) } returns gameState
  }

  @Test
  fun `transform detracts from the payer's balance`() {
    transformation.execute()
    verify { gameState.put(payersId, payer.detractFunds(liability)) }
  }

  @Test
  fun `transform adds the amount to the receiver's balance`() {
    transformation.execute()
    verify { gameState.put(receiversId, receiver.addFunds(liability)) }
  }

  @Test
  fun `transform only transfers what the payer has`() {
    val liability = payer.balance + BigInteger.ONE
    val transformation = PayLiability(payer, payersId, receiver, receiversId, liability, gameState)
    transformation.execute()
    verify { gameState.put(receiversId, receiver.addFunds(payersBalance)) }
    verify { gameState.put(payersId, payer.detractFunds(liability)) }
  }
}
