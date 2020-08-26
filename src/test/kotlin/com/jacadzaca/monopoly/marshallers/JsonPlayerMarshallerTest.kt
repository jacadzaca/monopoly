package com.jacadzaca.monopoly.marshallers

import com.jacadzaca.monopoly.*
import com.jacadzaca.monopoly.gamelogic.*
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import kotlin.random.*

internal class JsonPlayerMarshallerTest {
  private val marshaller = JsonPlayerMarshaller
  private val player = Player(Random.nextPositive(), Random.nextInt().toBigInteger())

  @Test
  fun `decode method is the inverse of the encode method`() {
    assertEquals(player, marshaller.decode(marshaller.encode(player)))
  }
}
