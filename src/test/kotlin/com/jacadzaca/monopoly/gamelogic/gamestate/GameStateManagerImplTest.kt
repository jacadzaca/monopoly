package com.jacadzaca.monopoly.gamelogic.gamestate

import com.jacadzaca.monopoly.createHouse
import com.jacadzaca.monopoly.createTile
import com.jacadzaca.monopoly.gamelogic.buildings.BuildingFactory
import com.jacadzaca.monopoly.gamelogic.buildings.BuildingType
import com.jacadzaca.monopoly.gamelogic.gamestate.events.MoveEvent
import com.jacadzaca.monopoly.gamelogic.gamestate.events.PropertyPurchaseEvent
import com.jacadzaca.monopoly.gamelogic.player.FundsManager
import com.jacadzaca.monopoly.gamelogic.player.PlayerMover
import com.jacadzaca.monopoly.gamelogic.tiles.TileManager
import com.jacadzaca.monopoly.getTestPlayer
import io.mockk.every
import io.mockk.mockk
import kotlinx.collections.immutable.persistentHashMapOf
import kotlinx.collections.immutable.toPersistentList
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class GameStateManagerImplTest {
  private val player = getTestPlayer()
  private lateinit var tileManager: TileManager
  private lateinit var fundsManager: FundsManager
  private lateinit var buildingFactory: BuildingFactory
  private lateinit var gameStateManager: GameStateManagerImpl
  private lateinit var playerMover: PlayerMover
  private val tiles = (1..4).map { createTile() }.toPersistentList()
  private val gameState = InMemoryGameState(persistentHashMapOf(player.id to player), tiles)


  @BeforeEach
  fun setUp() {
    tileManager = mockk(relaxed = true)
    buildingFactory = mockk()
    every { buildingFactory.getPriceFor(any()) } returns 100.toBigInteger()
    fundsManager = mockk(relaxed = true)
    playerMover = mockk()
    gameStateManager = GameStateManagerImpl(tileManager, fundsManager, buildingFactory, playerMover)
  }

  @Test
  fun `apply MoveEvent should update the mover's position`() {
    val event = MoveEvent(player.id)
    val movedPlayer = player.copy()
    every { playerMover.move(player, gameState.boardSize) } returns movedPlayer

    val expected = gameState.copy(players = persistentHashMapOf(event.playerId to movedPlayer))
    val actual = gameStateManager.applyEvent(event, gameState) as InMemoryGameState
    assertEquals(expected.players, actual.players)
  }

  @Test
  fun `apply PropertyPurchaseEvent should add a estate to the whereToBuy tile`() {
    val event = PropertyPurchaseEvent(player.id, BuildingType.HOUSE, 3)

    val tile = gameState.getTile(event.whereToBuy)
    val tileWithEstate = tile.copy(buildings = tile.buildings.add(createHouse()))
    every {
      tileManager.buyProperty(player, tile, any())
    } returns tileWithEstate

    val expected = gameState.copy(tiles = tiles.set(event.whereToBuy, tileWithEstate))
    val actual = gameStateManager.applyEvent(event, gameState) as InMemoryGameState

    assertEquals(expected.tiles, actual.tiles)
  }

  @Test
  fun `apply PropertyPurchaseEvent should detract from the buyer's balance`() {
    val event = PropertyPurchaseEvent(player.id, BuildingType.HOUSE, 3)

    val playerWithDetractedFunds = player.copy(balance = player.balance - buildingFactory.getPriceFor(BuildingType.HOUSE))
    every {
      fundsManager.detractFunds(player, buildingFactory.getPriceFor(BuildingType.HOUSE))
    } returns playerWithDetractedFunds

    val expected = gameState.copy(players = persistentHashMapOf(event.playerId to playerWithDetractedFunds))
    val actual = gameStateManager.applyEvent(event, gameState) as InMemoryGameState

    assertEquals(expected.players, actual.players)
  }
}
