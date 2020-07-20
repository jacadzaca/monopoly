package com.jacadzaca.monopoly.gamelogic.tiles

import com.jacadzaca.monopoly.gamelogic.Delta
import com.jacadzaca.monopoly.gamelogic.estates.Estate
import com.jacadzaca.monopoly.gamelogic.player.PlayerID
import java.math.BigInteger

/**
 * tileDelta.changeInPrice > 0, means an increase in the tile's price
 * tileDelta.changeInPrice < 0, means an decrease in the tile's price
 */
data class TileDelta(
  val changeInEstates: List<Estate>? = null,
  val changeInPrice: BigInteger? = null,
  val changeInOwner: PlayerID? = null
) : Delta
