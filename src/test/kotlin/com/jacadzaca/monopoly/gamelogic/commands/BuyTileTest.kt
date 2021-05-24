package com.jacadzaca.monopoly.gamelogic.commands

import com.jacadzaca.monopoly.gamelogic.GameState
import com.jacadzaca.monopoly.gamelogic.Player
import com.jacadzaca.monopoly.gamelogic.Tile
import com.jacadzaca.monopoly.nextPositive
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.UUID
import kotlin.random.Random

internal class BuyTileTest {
    private val buyer = mockk<Player>()
    private val buyersId = UUID.randomUUID()
    private val tile = mockk<Tile>()
    private val tileIndex = Random.nextPositive()
    private val gameState = mockk<GameState>()
    private val purchase = BuyTile(buyer, buyersId, tile, tileIndex, gameState)

    @BeforeEach
    fun setUp() {
        clearAllMocks()
        every { buyer.balance - tile.price } returns Random.nextPositive().toBigInteger()
        every { gameState.updateTile(tileIndex, newOwner = any()) } returns gameState
        every { gameState.updatePlayer(buyersId, newBalance = any()) } returns gameState
    }

    @Test
    fun `execute detracts from buyer's balance`() {
        val newBalance = buyer.balance - tile.price
        purchase.execute()
        verify { gameState.updatePlayer(buyersId, newBalance = newBalance) }
    }

    @Test
    fun `execute changes tile's owner`() {
        purchase.execute()
        verify { gameState.updateTile(tileIndex, newOwner = buyersId) }
    }
}
