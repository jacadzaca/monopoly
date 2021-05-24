package com.jacadzaca.monopoly.gamelogic.commands

import com.jacadzaca.monopoly.gamelogic.Estate
import com.jacadzaca.monopoly.gamelogic.GameState
import com.jacadzaca.monopoly.gamelogic.Player
import com.jacadzaca.monopoly.nextPositive
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.UUID
import kotlin.random.Random

internal class BuyEstateTest {
    private val tileIndex = Random.nextInt()
    private val buyersId = UUID.randomUUID()
    private val gameState = mockk<GameState>()
    private val buyer = mockk<Player>()
    private val estate = mockk<Estate>()
    private val purchase = BuyEstate(buyer, buyersId, tileIndex, estate, gameState)

    @BeforeEach
    fun setUp() {
        clearAllMocks()
        every { buyer.balance - estate.price } returns Random.nextPositive().toBigInteger()
        every { gameState.updatePlayer(buyersId, newBalance = any()) } returns gameState
        every { gameState.updateTile(tileIndex, newEstate = any()) } returns gameState
    }

    @Test
    fun `transform adds a estate to the tile`() {
        purchase.execute()
        verify { gameState.updateTile(tileIndex, newEstate = estate) }
    }

    @Test
    fun `transform detracts from the buyer's balance`() {
        val newBalance = buyer.balance - estate.price
        purchase.execute()
        verify { gameState.updatePlayer(buyersId, newBalance = newBalance) }
    }
}
