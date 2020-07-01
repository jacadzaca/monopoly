package com.jacadzaca.monopoly.gamelogic.tiles

import com.jacadzaca.monopoly.gamelogic.Delta
import com.jacadzaca.monopoly.gamelogic.buildings.Building
import com.jacadzaca.monopoly.gamelogic.player.PlayerID
import java.math.BigInteger

/**
 * tileDelta.changeInPrice > 0, means an increase in the tile's price
 * tileDelta.changeInPrice < 0, means an decrease in the tile's price
 */
data class TileDelta(
  val changeInBuildings: List<Building>? = null,
  val changeInPrice: BigInteger? = null,
  val changeInOwner: PlayerID? = null
) : Delta
