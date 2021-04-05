@file:UseSerializers(UUIDSerializer::class)
package com.jacadzaca.monopoly.requests

import com.jacadzaca.monopoly.serializers.*
import kotlinx.serialization.*
import java.util.*

@Serializable
data class Request(val action: PlayerAction, val requestersId: UUID, val changeTurn: Boolean = true)
