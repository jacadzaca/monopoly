package com.jacadzaca.monopoly.gamelogic

import com.jacadzaca.monopoly.getTestPlayer
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

internal class FundsManagerImplTest {
  private lateinit var player: Player
  private lateinit var fundsManager: FundsManagerImpl
  private var howMuch = 123.toBigInteger()

  @BeforeEach
  fun setUp() {
    player = getTestPlayer()
    fundsManager = FundsManagerImpl()
  }

  @Test
  fun `addFunds should add to player's balance`() {
    assertEquals(player.balance + howMuch,
      fundsManager.addFunds(player, howMuch).balance)
  }

  @Test
  fun `addFunds should throw IllegalArgument if howMuch is negative`() {
    howMuch = (-123).toBigInteger()
    assertThrows<IllegalArgumentException> {
      fundsManager.addFunds(player, howMuch)
    }
  }

  @Test
  fun `detractFunds should thor IllegalArgument if howMuch is negative`() {
    howMuch = (-123).toBigInteger()
    assertThrows<IllegalArgumentException> {
      fundsManager.detractFunds(player, howMuch)
    }
  }

  @Test
  fun `detractFunds should detract from player's balance`() {
    assertEquals(player.balance - howMuch,
      fundsManager.detractFunds(player, howMuch).balance)
  }

  @Test
  fun `detractFunds should throw an IllegalArgument if user is trying to extract more than the player has`() {
    howMuch = player.balance + howMuch
    assertThrows<IllegalArgumentException> {
      fundsManager.detractFunds(player, howMuch)
    }
  }
}
