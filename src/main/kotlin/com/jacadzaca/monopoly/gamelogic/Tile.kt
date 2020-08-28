@file:UseSerializers(BigIntegerSerializer::class, UUIDSerializer::class, PersistentEstateListSerializer::class)

package com.jacadzaca.monopoly.gamelogic

import com.jacadzaca.monopoly.serializers.*
import kotlinx.collections.immutable.PersistentList
import kotlinx.serialization.*
import java.math.BigInteger
import java.util.*

@Serializable
data class Tile(
  val houses: PersistentList<Estate>,
  val hotels: PersistentList<Estate>,
  val price: BigInteger,
  val ownersId: UUID?
) {
  fun addEstate(newEstate: Estate): Tile {
    return when (newEstate) {
      is Estate.House -> copy(houses = houses.add(newEstate))
      is Estate.Hotel -> copy(hotels = hotels.add(newEstate))
    }
  }

  fun totalRent(): BigInteger = rentFor(houses) + rentFor(hotels)

  private fun rentFor(estates: List<Estate>): BigInteger {
    return estates
      .map(Estate::rent)
      // it is impossible to reduce an empty collection
      .ifEmpty { listOf(BigInteger.ZERO) }
      .reduce(BigInteger::add)
  }

  fun houseCount(): Int = houses.size

  fun changeOwner(newOwner: UUID): Tile = copy(ownersId = newOwner)
}
