package com.jacadzaca.monopoly.gamelogic.transformations

import com.jacadzaca.monopoly.gamelogic.GameState

interface Transformation {
  fun transform(): GameState
}
