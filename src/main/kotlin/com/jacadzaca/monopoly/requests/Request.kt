@file:UseSerializers(UUIDSerializer::class)
package com.jacadzaca.monopoly.requests

import com.jacadzaca.monopoly.serializers.UUIDSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import java.util.UUID

@Serializable
data class Request(val action: PlayerAction, val requestersId: UUID, val changeTurn: Boolean = true)
