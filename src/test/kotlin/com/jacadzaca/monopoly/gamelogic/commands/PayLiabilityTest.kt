package com.jacadzaca.monopoly.gamelogic.commands

import com.jacadzaca.monopoly.*
import com.jacadzaca.monopoly.gamelogic.*
import io.mockk.*
import org.junit.jupiter.api.*
import java.math.*
import java.util.*
import kotlin.random.Random

internal class PayLiabilityTest {
  private val payer = mockk<Player>()
  private val receiver = mockk<Player>()
  private val payersId = UUID.randomUUID()
  private val receiversId = UUID.randomUUID()
  private val gameState = mockk<GameState>()
  private val payersBalance = Random.nextPositive().toBigInteger()
  private val liability = payersBalance - BigInteger.ONE
  private val command = PayLiability(payer, payersId, receiver, receiversId, liability, gameState)

  @BeforeEach
  fun setUp() {
    clearAllMocks()
    every { payer.balance } returns payersBalance
    every { receiver.balance } returns Random.nextPositive().toBigInteger()
    every { gameState.updatePlayer(payersId, newBalance = any()) } returns gameState
    every { gameState.updatePlayer(receiversId, newBalance = any()) } returns gameState
  }

  @Test
  fun `transform detracts from the payer's balance`() {
    command.execute()
    val newBalance = payer.balance - liability
    verify { gameState.updatePlayer(payersId, newBalance =  newBalance) }
  }

  @Test
  fun `transform adds the amount to the receiver's balance`() {
    command.execute()
    val newBalance = receiver.balance + liability
    verify { gameState.updatePlayer(receiversId, newBalance = newBalance) }
  }

  @Test
  fun `transform only transfers what the payer has`() {
    val liability = payer.balance + BigInteger.ONE
    val command = PayLiability(payer, payersId, receiver, receiversId, liability, gameState)
    val receiversBalance = receiver.balance + payersBalance
    val payersBalance = payersBalance - liability
    command.execute()
    verify { gameState.updatePlayer(receiversId, newBalance = receiversBalance) }
    verify { gameState.updatePlayer(payersId, newBalance = payersBalance) }
  }
}
