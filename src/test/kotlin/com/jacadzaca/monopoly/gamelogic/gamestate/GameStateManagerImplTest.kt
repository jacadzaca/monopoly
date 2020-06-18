package com.jacadzaca.monopoly.gamelogic.gamestate

import com.jacadzaca.monopoly.createHouse
import com.jacadzaca.monopoly.createLiability
import com.jacadzaca.monopoly.createTile
import com.jacadzaca.monopoly.gamelogic.buildings.BuildingFactory
import com.jacadzaca.monopoly.gamelogic.buildings.BuildingType
import com.jacadzaca.monopoly.gamelogic.gamestate.events.MoveEvent
import com.jacadzaca.monopoly.gamelogic.gamestate.events.PlayerPaysLiabilityEvent
import com.jacadzaca.monopoly.gamelogic.gamestate.events.PropertyPurchaseEvent
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
import java.math.BigInteger

internal class GameStateManagerImplTest {
  private val player = getTestPlayer()
  private val receiver = getTestPlayer()
  private lateinit var tileManager: TileManager
  private lateinit var buildingFactory: BuildingFactory
  private lateinit var gameStateManager: GameStateManagerImpl
  private lateinit var playerMover: PlayerMover
  private val tiles = (1..4).map { createTile() }.toPersistentList()
  private val gameState = InMemoryGameState(persistentHashMapOf(player.id to player, receiver.id to receiver), tiles)


  @BeforeEach
  fun setUp() {
    tileManager = mockk(relaxed = true)
    buildingFactory = mockk()
    every { buildingFactory.getPriceFor(any()) } returns 100.toBigInteger()
    playerMover = mockk()
    gameStateManager = GameStateManagerImpl(tileManager, buildingFactory, playerMover)
  }

  @Test
  fun `apply MoveEvent should update the mover's position`() {
    val event = MoveEvent(player.id)
    val movedPlayer = player.copy(position = 10)
    every { playerMover.move(player, gameState.boardSize) } returns movedPlayer

    val actual = gameStateManager.applyEvent(event, gameState)

    assertEquals(movedPlayer.position, actual.getPlayer(player.id).position)
  }

  @Test
  fun `apply PropertyPurchaseEvent should add a estate to the whereToBuy tile`() {
    val event = PropertyPurchaseEvent(player.id, BuildingType.HOUSE, 3)

    val tile = gameState.getTile(event.whereToBuy)
    val tileWithEstate = tile.copy(buildings = tile.buildings.add(createHouse()))
    every {
      tileManager.buyProperty(player, tile, any())
    } returns tileWithEstate

    val actual = gameStateManager.applyEvent(event, gameState)

    assertEquals(tileWithEstate.buildings, actual.getTile(event.whereToBuy).buildings)
  }

  @Test
  fun `apply PropertyPurchaseEvent should detract from the buyer's balance`() {
    val event = PropertyPurchaseEvent(player.id, BuildingType.HOUSE, 3)

    val playerWithDetractedFunds = player.detractFunds(buildingFactory.getPriceFor(BuildingType.HOUSE))

    val actual = gameStateManager.applyEvent(event, gameState)

    assertEquals(playerWithDetractedFunds.balance, actual.getPlayer(player.id).balance)
  }

  @Test
  fun `apply PlayerPayLiabilityEvent should detract from the payer's balance`() {
    val event = PlayerPaysLiabilityEvent(player.id, createLiability(receiver.id, 100.toBigInteger()))
    val playerWithDetractedFunds = player.detractFunds(event.liability.howMuch)

    val actual = gameStateManager.applyEvent(event, gameState)

    assertEquals(playerWithDetractedFunds.balance, actual.getPlayer(player.id).balance)
  }

  @Test
  fun `apply PlayerPayLiabilityEvent should add the amount to receiver's balance`() {
    val event = PlayerPaysLiabilityEvent(player.id, createLiability(receiver.id, 100.toBigInteger()))
    val receiverWithAddedFunds = receiver.addFunds(event.liability.howMuch)

    val actual = gameStateManager.applyEvent(event, gameState)

    assertEquals(receiverWithAddedFunds.balance, actual.getPlayer(receiver.id).balance)
  }

  @Test
  fun `apply PlayerPayLiabilityEvent should only transfer what the payer has`() {
    val event = PlayerPaysLiabilityEvent(player.id, createLiability(
      receiver.id, player.balance + BigInteger.TEN))
    val receiverWithAddedFunds = receiver.addFunds(player.balance)

    val actual = gameStateManager.applyEvent(event, gameState)

    assertEquals(receiverWithAddedFunds.balance, actual.getPlayer(receiver.id).balance)
  }

}
