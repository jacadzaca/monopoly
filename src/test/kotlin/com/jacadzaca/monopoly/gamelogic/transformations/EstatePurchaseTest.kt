package com.jacadzaca.monopoly.gamelogic.transformations

import com.jacadzaca.monopoly.gamelogic.GameState
import com.jacadzaca.monopoly.gamelogic.Player
import com.jacadzaca.monopoly.gamelogic.Tile
import com.jacadzaca.monopoly.gamelogic.Estate
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.random.Random

internal class EstatePurchaseTest {
  private val tileIndex = Random.nextInt()
  private val buyersId = UUID.randomUUID()
  private val gameState = mockk<GameState>()
  private val tile = mockk<Tile>(relaxed = true)
  private val buyer = mockk<Player>(relaxed = true)
  private val estate = mockk<Estate>(name = "estate")
  private val purchase = EstatePurchase(buyer, buyersId, tile, tileIndex, estate, gameState)

  @BeforeEach
  fun setUp() {
    clearAllMocks()
    every { estate.price } returns mockk()
    every { gameState.update(buyersId, any()) } returns gameState
    every { gameState.update(tileIndex, any()) } returns gameState
    every { gameState.addTransformation(any()) } returns gameState
  }

  @Test
  fun `transform adds a estate to the tile`() {
    val tileWithAddedHouse = mockk<Tile>(name = "tile with added estate")
    every { tile.addEstate(estate) } returns tileWithAddedHouse
    purchase.transform()
    verify { gameState.update(tileIndex, tileWithAddedHouse) }
  }

  @Test
  fun `transform detracts from the buyer's balance`() {
    val estatePrice = Random.nextInt().toBigInteger()
    val playerAfterPurchase = mockk<Player>(name = "playerAfterPurchase")
    every { estate.price } returns estatePrice
    every { buyer.detractFunds(estatePrice) } returns playerAfterPurchase
    purchase.transform()
    verify { gameState.update(buyersId, playerAfterPurchase) }
  }
}
