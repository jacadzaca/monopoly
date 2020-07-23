package com.jacadzaca.monopoly.gamelogic.tiles

import com.jacadzaca.monopoly.gamelogic.estates.Estate
import com.jacadzaca.monopoly.gamelogic.estates.EstateType
import com.jacadzaca.monopoly.gamelogic.player.PlayerID
import kotlinx.collections.immutable.PersistentList
import java.math.BigInteger

data class Tile(
  val estates: PersistentList<Estate>,
  val price: BigInteger,
  val owner: PlayerID?
) {
  fun addEstate(newEstate: Estate): Tile = copy(estates = estates.add(newEstate))

  fun changeOwner(newOwner: PlayerID): Tile = copy(owner = newOwner)

  fun totalRent(): BigInteger = estates.map(Estate::rent).reduce(BigInteger::add)

  fun houseCount(): Int = estates.filter { it.estateType == EstateType.HOUSE }.count()
}
