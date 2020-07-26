package com.jacadzaca.monopoly.gamelogic

import com.jacadzaca.monopoly.gamelogic.estates.Estate
import com.jacadzaca.monopoly.gamelogic.estates.EstateType
import kotlinx.collections.immutable.PersistentList
import java.math.BigInteger
import java.util.*

data class Tile(
  val estates: PersistentList<Estate>,
  val price: BigInteger,
  val ownersId: UUID?
) {
  fun addEstate(newEstate: Estate): Tile = copy(estates = estates.add(newEstate))

  fun changeOwner(newOwner: UUID): Tile = copy(ownersId = newOwner)

  fun totalRent(): BigInteger = estates.map(Estate::rent).reduce(BigInteger::add)

  fun houseCount(): Int = estates.count { it.estateType == EstateType.HOUSE }
}
