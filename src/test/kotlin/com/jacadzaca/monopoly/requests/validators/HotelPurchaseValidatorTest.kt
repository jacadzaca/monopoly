package com.jacadzaca.monopoly.requests.validators

import com.jacadzaca.monopoly.Computation
import com.jacadzaca.monopoly.gamelogic.Estate
import com.jacadzaca.monopoly.gamelogic.GameState
import com.jacadzaca.monopoly.gamelogic.Player
import com.jacadzaca.monopoly.gamelogic.Tile
import com.jacadzaca.monopoly.gamelogic.commands.BuyEstate
import com.jacadzaca.monopoly.nextPositive
import com.jacadzaca.monopoly.requests.PlayerAction
import com.jacadzaca.monopoly.requests.validators.HotelPurchaseValidator.Companion.NOT_ENOUGH_HOUSES
import com.jacadzaca.monopoly.requests.validators.RequestValidator.Companion.BUYER_HAS_INSUFFICIENT_BALANCE
import com.jacadzaca.monopoly.requests.validators.RequestValidator.Companion.NOT_PLAYERS_TURN
import com.jacadzaca.monopoly.requests.validators.RequestValidator.Companion.TILE_NOT_OWNED_BY_BUYER
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.math.BigInteger
import java.util.UUID
import kotlin.random.Random

internal class HotelPurchaseValidatorTest {
    private val tile = mockk<Tile>()
    private val buyer = mockk<Player>()
    private val buyersId = UUID.randomUUID()
    private val gameState = mockk<GameState>()
    private val buyersPosition = Random.nextPositive()
    private val requiredHousesForHotel = Random.nextPositive()
    private val createPurchase = mockk<(Player, UUID, Int, Estate, GameState) -> BuyEstate>()
    private val hotel = mockk<Estate.Hotel>(name = "hotel")
    private val action = mockk<PlayerAction.BuyHotelAction>()
    private val request = HotelPurchaseValidator(hotel, requiredHousesForHotel, createPurchase)

    @BeforeEach
    fun setUp() {
        every { tile.ownersId } returns buyersId
        every { buyer.position } returns buyersPosition
        every { gameState.players[buyersId] } returns buyer
        every { gameState.tiles[buyersPosition] } returns tile
        every { tile.houseCount() } returns requiredHousesForHotel
        every { buyer.balance } returns Random.nextPositive().toBigInteger()
        every { hotel.price } returns buyer.balance - BigInteger.ONE
        every { gameState.isPlayersTurn(buyersId) } returns true
    }

    @Test
    fun `validate returns Success if the buyer is the tile's owner, has sufficient funds and the tile has sufficient number of houses`() {
        val createdEstatePurchase = mockk<BuyEstate>()
        every { createPurchase(buyer, buyersId, buyersPosition, hotel, gameState) } returns createdEstatePurchase
        every { tile.ownersId } returns buyersId
        every { hotel.price } returnsMany listOf(
            buyer.balance,
            buyer.balance - BigInteger.ONE,
            buyer.balance - Random.nextInt(1, buyer.balance.toInt()).toBigInteger()
        )
        every { tile.houseCount() } returnsMany listOf(
            requiredHousesForHotel,
            requiredHousesForHotel + 1,
            Random.nextPositive(from = requiredHousesForHotel)
        )
        val success = Computation.success(createdEstatePurchase)
        assertEquals(success, request.validate(buyersId, action, gameState))
        assertEquals(success, request.validate(buyersId, action, gameState))
        assertEquals(success, request.validate(buyersId, action, gameState))
    }

    @Test
    fun `validate returns Failure if the buyer dose not own the tile`() {
        every { tile.ownersId } returnsMany listOf(
            UUID.randomUUID(),
            null
        )
        assertEquals(TILE_NOT_OWNED_BY_BUYER, request.validate(buyersId, action, gameState))
        assertEquals(TILE_NOT_OWNED_BY_BUYER, request.validate(buyersId, action, gameState))
    }

    @Test
    fun `validate returns Failure if the buyer has insufficient funds`() {
        every { hotel.price } returnsMany listOf(
            buyer.balance + BigInteger.ONE,
            buyer.balance + Random.nextPositive().toBigInteger()
        )
        assertEquals(BUYER_HAS_INSUFFICIENT_BALANCE, request.validate(buyersId, action, gameState))
        assertEquals(BUYER_HAS_INSUFFICIENT_BALANCE, request.validate(buyersId, action, gameState))
    }

    @Test
    fun `validate returns Failure if there are too few houses on tile`() {
        every { tile.houseCount() } returnsMany listOf(
            requiredHousesForHotel - 1,
            Random.nextPositive(until = requiredHousesForHotel)
        )
        assertEquals(NOT_ENOUGH_HOUSES, request.validate(buyersId, action, gameState))
        assertEquals(NOT_ENOUGH_HOUSES, request.validate(buyersId, action, gameState))
    }

    @Test
    fun `validate returns Failure if it is not the buyer's turn`() {
        every { gameState.isPlayersTurn(buyersId) } returns false
        assertEquals(NOT_PLAYERS_TURN, request.validate(buyersId, action, gameState))
    }
}
