package com.jacadzaca.monopoly.gameroom

import com.jacadzaca.monopoly.GameAction
import com.jacadzaca.monopoly.Piece
import com.jacadzaca.monopoly.Player
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*

internal class GameActionJudgeImplTest {
    private lateinit var gameActionJudge: GameActionJudgeImpl

    @BeforeEach
    fun setUp() {
      gameActionJudge = GameActionJudgeImpl()
    }

  @Test
  fun `canPlayerExecuteAction should return false if committer's id and player's id differ`() {
    val player = getTestPlayer()
    val gameAction = getTestGameAction()
    assertFalse(gameActionJudge.canPlayerExecuteAction(player, gameAction))
  }

  private fun getTestGameAction(): GameAction = GameAction(UUID.randomUUID(), 1)

  private fun getTestPlayer(): Player = Player(UUID.randomUUID(), Piece())
}
