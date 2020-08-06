package com.jacadzaca.monopoly.gamelogic.estatepurchase

import com.jacadzaca.monopoly.gamelogic.GameState
import com.jacadzaca.monopoly.gamelogic.Player
import com.jacadzaca.monopoly.gamelogic.Tile
import com.jacadzaca.monopoly.gamelogic.estates.Estate
import com.jacadzaca.monopoly.gamelogic.estates.EstateType
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
import java.math.BigInteger
import java.util.*
import kotlin.random.Random

internal class EstatePurchaseTest {
  private val tileIndex = Random.nextInt()
  private val buyersId = UUID.randomUUID()
  private val gameState = mockk<GameState>()
  private val tile = mockk<Tile>(relaxed = true)
  private val buyer = mockk<Player>(relaxed = true)
  private val create = mockk<(EstateType) -> Estate>()
  private val priceOf = mockk<(EstateType) -> BigInteger>()

  @BeforeEach
  fun setUp() {
    clearMocks(gameState, tile, buyer, create, priceOf)
    every { priceOf(any()) } returns mockk()
    every { create(any()) } returns mockk()
    every { gameState.update(buyersId, any()) } returns gameState
    every { gameState.update(tileIndex, any()) } returns gameState
  }

  @ParameterizedTest
  @EnumSource(EstateType::class)
  fun `apply adds a estate to the tile`(estateType: EstateType) {
    val estate = mockk<Estate>(name = "estate")
    val tileWithAddedHouse = mockk<Tile>(name = "tile with added estate")
    every { tile.addEstate(estate) } returns tileWithAddedHouse
    every { create(estateType) } returns estate
    purchaseOfType(estateType).apply(gameState)
    verify { gameState.update(tileIndex, tileWithAddedHouse) }
  }

  @ParameterizedTest
  @EnumSource(EstateType::class)
  fun `apply detracts from the buyer's balance`(estateType: EstateType) {
    val estatePrice = Random.nextInt().toBigInteger()
    val playerAfterPurchase = mockk<Player>(name = "playerAfterPurchase")
    every { priceOf(estateType) } returns estatePrice
    every { buyer.detractFunds(estatePrice) } returns playerAfterPurchase
    purchaseOfType(estateType).apply(gameState)
    verify { gameState.update(buyersId, playerAfterPurchase) }
  }

  private fun purchaseOfType(estateType: EstateType): EstatePurchase =
    EstatePurchase(buyer, buyersId, tile, tileIndex, estateType, create, priceOf)
}
