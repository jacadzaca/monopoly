package com.jacadzaca.monopoly.gamelogic.gamestate

import com.jacadzaca.monopoly.createHouse
import com.jacadzaca.monopoly.createLiability
import com.jacadzaca.monopoly.createTile
import com.jacadzaca.monopoly.gamelogic.estates.EstateFactory
import com.jacadzaca.monopoly.gamelogic.estates.EstateType
import com.jacadzaca.monopoly.gamelogic.gamestate.events.MoveEvent
import com.jacadzaca.monopoly.gamelogic.gamestate.events.PlayerPaysLiabilityEvent
import com.jacadzaca.monopoly.gamelogic.gamestate.events.estatepurchase.EstatePurchaseEvent
import com.jacadzaca.monopoly.gamelogic.gamestate.events.tilepurchase.TilePurchaseEvent
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
import org.junit.jupiter.api.assertThrows
import java.math.BigInteger

internal class GameStateManagerImplTest {
  private val player = getTestPlayer()
  private val receiver = getTestPlayer()
  private lateinit var tileManager: TileManager
  private lateinit var estateFactory: EstateFactory
  private lateinit var gameStateManager: GameStateManagerImpl
  private lateinit var playerMover: PlayerMover
  private val tiles = (1..4).map { createTile() }.toPersistentList()
  private val gameState = GameState(persistentHashMapOf(player.id to player, receiver.id to receiver), tiles)


  @BeforeEach
  fun setUp() {
    tileManager = mockk(relaxed = true)
    estateFactory = mockk()
    every { estateFactory.getPriceFor(any()) } returns 100.toBigInteger()
    playerMover = mockk()
    gameStateManager = GameStateManagerImpl(tileManager, estateFactory, playerMover)
  }

  @Test
  fun `apply MoveEvent should update the mover's position`() {
    val event = MoveEvent(player.id)
    val movedPlayer = player.copy(position = 10)
    every { playerMover.move(player, gameState.boardSize) } returns movedPlayer

    val actual = gameStateManager.applyPlayerMove(event, gameState)

    assertEquals(movedPlayer.position, actual.players.getValue(player.id).position)
  }

  @Test
  fun `apply TilePurchaseEvent should change the tile's owner`() {
    val event =
      TilePurchaseEvent(player.id, 2)
    val tileOwnedByBuyer = gameState.tiles[event.tileIndex].copy(owner = player.id)
    every { tileManager.buyTile(player, gameState.tiles[event.tileIndex]) } returns tileOwnedByBuyer
    val actual = gameStateManager.applyTilePurchase(event, gameState)
    assertEquals(tileOwnedByBuyer.owner, actual.tiles[event.tileIndex].owner)
  }

  @Test
  fun `apply TilePurchaseEvent should detract from the buyer's balance`() {
    val event =
      TilePurchaseEvent(player.id, 2)
    val price = gameState.tiles[event.tileIndex].price
    val buyerWithDetractedFunds = player.detractFunds(price)
    val actual = gameStateManager.applyTilePurchase(event, gameState)
    assertEquals(buyerWithDetractedFunds.balance, actual.players.getValue(player.id).balance)
  }

  @Test
  fun `apply TilePurchaseEvent throws IllegalArgument if buyer has insufficient funds`() {
    val event =
      TilePurchaseEvent(player.id, 3)
    val price = gameState.tiles[event.tileIndex].price
    val buyer = player.copy(balance = price - BigInteger.TEN)
    every { tileManager.buyTile(buyer, gameState.tiles[event.tileIndex]) } throws IllegalArgumentException()
    assertThrows<IllegalArgumentException> {
      gameStateManager.applyTilePurchase(event, gameState.update(player.id, buyer))
    }
  }

  @Test
  fun `apply TilePurchaseEvent throws IllegalArgument if buyer already owns the tile`() {
    val event =
      TilePurchaseEvent(player.id, 3)
    val tileToBeBought = gameState.tiles[event.tileIndex].copy(owner = event.playerId)
    every { tileManager.buyTile(player, tileToBeBought) } throws IllegalArgumentException()
    assertThrows<IllegalArgumentException> {
      gameStateManager.applyTilePurchase(event, gameState.update(event.tileIndex, tileToBeBought))
    }
  }

  @Test
  fun `apply PropertyPurchaseEvent should add a estate to the whereToBuy tile`() {
    val event =
      EstatePurchaseEvent(
        player.id,
        EstateType.HOUSE,
        3
      )

    val tile = gameState.tiles[event.tileIndex]
    val tileWithEstate = tile.copy(estates = tile.estates.add(createHouse()))
    every {
      tileManager.buyProperty(player, tile, any())
    } returns tileWithEstate

    val actual = gameStateManager.applyEstatePurchase(event, gameState)

    assertEquals(tileWithEstate.estates, actual.tiles[event.tileIndex].estates)
  }

  @Test
  fun `apply PropertyPurchaseEvent should detract from the buyer's balance`() {
    val event =
      EstatePurchaseEvent(
        player.id,
        EstateType.HOUSE,
        3
      )

    val playerWithDetractedFunds = player.detractFunds(estateFactory.getPriceFor(EstateType.HOUSE))

    val actual = gameStateManager.applyEstatePurchase(event, gameState)

    assertEquals(playerWithDetractedFunds.balance, actual.players.getValue(player.id).balance)
  }

  @Test
  fun `apply PlayerPayLiabilityEvent should detract from the payer's balance`() {
    val event = PlayerPaysLiabilityEvent(player.id, createLiability(receiver.id, 100.toBigInteger()))
    val playerWithDetractedFunds = player.detractFunds(event.liability.howMuch)

    val actual = gameStateManager.applyLiabilityPayment(event, gameState)

    assertEquals(playerWithDetractedFunds.balance, actual.players.getValue(player.id).balance)
  }

  @Test
  fun `apply PlayerPayLiabilityEvent should add the amount to receiver's balance`() {
    val event = PlayerPaysLiabilityEvent(player.id, createLiability(receiver.id, 100.toBigInteger()))
    val receiverWithAddedFunds = receiver.addFunds(event.liability.howMuch)

    val actual = gameStateManager.applyLiabilityPayment(event, gameState)

    assertEquals(receiverWithAddedFunds.balance, actual.players.getValue(receiver.id).balance)
  }

  @Test
  fun `apply PlayerPayLiabilityEvent should only transfer what the payer has`() {
    val event = PlayerPaysLiabilityEvent(player.id, createLiability(
      receiver.id, player.balance + BigInteger.TEN))
    val receiverWithAddedFunds = receiver.addFunds(player.balance)

    val actual = gameStateManager.applyLiabilityPayment(event, gameState)

    assertEquals(receiverWithAddedFunds.balance, actual.players.getValue(receiver.id).balance)
  }

}
