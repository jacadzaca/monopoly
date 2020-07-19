package com.jacadzaca.monopoly.gamelogic.gamestate.events

import com.jacadzaca.monopoly.createHotel
import com.jacadzaca.monopoly.createHouse
import com.jacadzaca.monopoly.createTile
import com.jacadzaca.monopoly.gamelogic.buildings.Building
import com.jacadzaca.monopoly.gamelogic.buildings.BuildingFactory
import com.jacadzaca.monopoly.gamelogic.buildings.BuildingType
import com.jacadzaca.monopoly.gamelogic.gamestate.GameState
import com.jacadzaca.monopoly.gamelogic.player.PlayerID
import com.jacadzaca.monopoly.gamelogic.tiles.Tile
import com.jacadzaca.monopoly.getTestPlayer
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.*
import java.math.BigInteger
import java.util.*
import java.util.stream.Stream

internal class GameEventVerifierImplTest {
  companion object {
    private val buyer = getTestPlayer()
    private const val requiredHousesForHotel = 2
    private const val tileIndex = 0
  }

  private val tile = mockk<Tile>()
  private val gameState = mockk<GameState>()
  private val buildingFactory = mockk<BuildingFactory>()
  private val gameEventVerifier = GameEventVerifierImpl(buildingFactory, requiredHousesForHotel)

  @BeforeEach
  fun setUp() {
    every { gameState.players.getValue(buyer.id) } returns buyer
    every { buildingFactory.getPriceFor(BuildingType.HOUSE) } returns buyer.balance - 10.toBigInteger()
    every { buildingFactory.getPriceFor(BuildingType.HOTEL) } returns buyer.balance - 1000.toBigInteger()

    every { tile.owner } returns buyer.id
    every { tile.houseCount() } returns requiredHousesForHotel
  }

  @Test
  fun `verifyTilePurchase returns inputted event if the buyer has sufficient funds and the tile has no owner`() {
    val event = TilePurchaseEvent(buyer.id, tileIndex)
    every { tile.owner } returns null
    every { tile.price } returnsMany listOf(buyer.balance, buyer.balance - BigInteger.ONE)
    every { gameState.tiles[event.tileIndex] } returns tile
    assertSame(event, gameEventVerifier.verifyTilePurchaseEvent(event, gameState))
  }

  @ParameterizedTest
  @ArgumentsSource(AlreadyHasOwnerProvider::class)
  fun `verifyTilePurchaseEvent returns null if the the tile already has an owner`(otherPlayer: PlayerID) {
    val event = TilePurchaseEvent(buyer.id, tileIndex)
    every { tile.owner } returns otherPlayer
    every { gameState.tiles[event.tileIndex] } returns tile
    assertNull(gameEventVerifier.verifyTilePurchaseEvent(event, gameState))
  }

  @Test
  fun `verifyTilePurchaseEvent returns null if the buyer's balance is less than the tile's price`() {
    val event = TilePurchaseEvent(buyer.id, tileIndex)
    every { tile.owner } returns null
    every { tile.price } returns buyer.balance + BigInteger.ONE
    every { gameState.tiles[event.tileIndex] } returns tile
    assertNull(gameEventVerifier.verifyTilePurchaseEvent(event, gameState))
  }

  @Test
  fun `verifyEstatePurchaseEvent returns the inputted event if the buyer is the tile's owner, the buyer has sufficient funds and the buyer wants a house`() {
    val event = PropertyPurchaseEvent(buyer.id, BuildingType.HOUSE, tileIndex)
    every { buildingFactory.getPriceFor(BuildingType.HOUSE) } returns buyer.balance - 10.toBigInteger()
    every { gameState.tiles[event.tileIndex] } returns tile
    assertSame(event, gameEventVerifier.verifyEstatePurchaseEvent(event, gameState))
  }

  @Test
  fun `verifyEstatePurchaseEvent returns the inputted event if the tile's owner is the buyer, the buyer has sufficient funds, there is sufficient number of houses and the buyer wants a hotel`() {
    val event = PropertyPurchaseEvent(buyer.id, BuildingType.HOTEL, tileIndex)
    every { tile.owner } returns buyer.id
    every { tile.houseCount() } returnsMany listOf(requiredHousesForHotel, requiredHousesForHotel + 1)
    every { gameState.tiles[event.tileIndex] } returns tile
    every { buildingFactory.getPriceFor(BuildingType.HOTEL) } returns buyer.balance - 10.toBigInteger()
    assertSame(event, gameEventVerifier.verifyEstatePurchaseEvent(event, gameState))
    assertSame(event, gameEventVerifier.verifyEstatePurchaseEvent(event, gameState))
  }

  @ParameterizedTest
  @ArgumentsSource(BuyerNotOwingTileProvider::class)
  fun `verifyEstatePurchaseEvent returns null if the buyer dose not own the tile`(estate: Building, otherPlayer: PlayerID?) {
    val event = PropertyPurchaseEvent(buyer.id, estate.buildingType, tileIndex)
    every { tile.owner } returns otherPlayer
    every { gameState.tiles[event.tileIndex] } returns tile
    assertNull(gameEventVerifier.verifyEstatePurchaseEvent(event, gameState))
  }

  @ParameterizedTest
  @ArgumentsSource(EstateProvider::class)
  fun `verifyEstatePurchaseEvent returns null if the buyer has insufficient funds`(estate: Building) {
    val event = PropertyPurchaseEvent(buyer.id, estate.buildingType, tileIndex)
    every { buildingFactory.getPriceFor(estate.buildingType) } returns buyer.balance + 10.toBigInteger()
    every { gameState.tiles[event.tileIndex] } returns tile
    assertNull(gameEventVerifier.verifyEstatePurchaseEvent(event, gameState))
  }

  @Test
  fun `verifyEstatePurchaseEvent returns null if the buyer wants a hotel and there are too few houses on tile`() {
    val event = PropertyPurchaseEvent(buyer.id, BuildingType.HOTEL, tileIndex)
    every { tile.houseCount() } returns requiredHousesForHotel - 1
    every { gameState.tiles[event.tileIndex] } returns tile
    assertNull(gameEventVerifier.verifyEstatePurchaseEvent(event, gameState))
  }

  private class EstateProvider : ArgumentsProvider {
    override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> {
      return Stream.of(
        Arguments.of(createHouse(), createHotel()))
    }
  }

  private class BuyerNotOwingTileProvider : ArgumentsProvider {
    override fun provideArguments(context: ExtensionContext?): Stream<out Arguments> {
      val otherOwner = UUID.randomUUID()
      return Stream.of(
        Arguments.of(createHouse(), otherOwner),
        Arguments.of(createHouse(), null),
        Arguments.of(createHotel(), otherOwner),
        Arguments.of(createHotel(), null))
    }
  }

  private class AlreadyHasOwnerProvider : ArgumentsProvider {
    override fun provideArguments(context: ExtensionContext): Stream<out Arguments> {
      val otherOwner = UUID.randomUUID()
      return Stream.of(Arguments.of(otherOwner), Arguments.of(buyer.id))
    }
  }
}
