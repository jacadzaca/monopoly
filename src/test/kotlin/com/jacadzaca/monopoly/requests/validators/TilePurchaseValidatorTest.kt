package com.jacadzaca.monopoly.requests.validators

import com.jacadzaca.monopoly.Computation
import com.jacadzaca.monopoly.gamelogic.GameState
import com.jacadzaca.monopoly.gamelogic.Player
import com.jacadzaca.monopoly.gamelogic.Tile
import com.jacadzaca.monopoly.gamelogic.commands.BuyTile
import com.jacadzaca.monopoly.nextPositive
import com.jacadzaca.monopoly.requests.PlayerAction
import com.jacadzaca.monopoly.requests.validators.RequestValidator.Companion.BUYER_HAS_INSUFFICIENT_BALANCE
import com.jacadzaca.monopoly.requests.validators.RequestValidator.Companion.INVALID_PLAYER_ID
import com.jacadzaca.monopoly.requests.validators.RequestValidator.Companion.NOT_PLAYERS_TURN
import com.jacadzaca.monopoly.requests.validators.TilePurchaseValidator.Companion.TILE_ALREADY_HAS_OWNER
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.math.BigInteger
import java.util.UUID
import kotlin.random.Random

internal class TilePurchaseValidatorTest {
    private val tile = mockk<Tile>()
    private val buyer = mockk<Player>()
    private val gameState = mockk<GameState>()
    private val buyersId = UUID.randomUUID()
    private val buyersPosition = Random.nextPositive()
    private val createPurchase = mockk<(Player, UUID, Tile, Int, GameState) -> (BuyTile)>()
    private val action = mockk<PlayerAction.BuyTileAction>()
    private val validator = TilePurchaseValidator(createPurchase)

    @BeforeEach
    fun setUp() {
        every { tile.ownersId } returns null
        every { buyer.position } returns buyersPosition
        every { gameState.tiles[buyersPosition] } returns tile
        every { gameState.players[buyersId] } returns buyer
        every { buyer.balance } returns Random.nextPositive().toBigInteger()
        every { gameState.isPlayersTurn(buyersId) } returns true
    }

    @Test
    fun `validate returns Success if the buyer has sufficient funds and the tile has no owner`() {
        every { tile.ownersId } returns null
        every { tile.price } returnsMany listOf(buyer.balance, buyer.balance - BigInteger.ONE)
        val purchase = mockk<BuyTile>(name = "purchase")
        every { createPurchase(buyer, buyersId, tile, buyersPosition, gameState) } returns purchase
        val success = Computation.success(purchase)
        assertEquals(success, validator.validate(buyersId, action, gameState))
        assertEquals(success, validator.validate(buyersId, action, gameState))
    }

    @Test
    fun `validate returns Failure if the the tile already has an owner`() {
        every { tile.price } returns buyer.balance - BigInteger.ONE
        every { tile.ownersId } returnsMany listOf(UUID.randomUUID(), buyersId)
        assertEquals(TILE_ALREADY_HAS_OWNER, validator.validate(buyersId, action, gameState))
        assertEquals(TILE_ALREADY_HAS_OWNER, validator.validate(buyersId, action, gameState))
    }

    @Test
    fun `validate returns Failure if the buyer's balance is less than the tile's price`() {
        every { tile.price } returns buyer.balance + BigInteger.ONE
        assertEquals(BUYER_HAS_INSUFFICIENT_BALANCE, validator.validate(buyersId, action, gameState))
    }

    @Test
    fun `validate returns Failure if the event references an non-existing player`() {
        every { gameState.players[buyersId] } returns null
        assertEquals(INVALID_PLAYER_ID, validator.validate(buyersId, action, gameState))
    }

    @Test
    fun `validate returns Failure if it is not the buyer's turn`() {
        every { gameState.isPlayersTurn(buyersId) } returns false
        assertEquals(NOT_PLAYERS_TURN, validator.validate(buyersId, action, gameState))
    }
}
