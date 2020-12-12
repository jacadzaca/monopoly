package com.jacadzaca.monopoly.gameroom

import com.jacadzaca.monopoly.*
import io.vertx.core.buffer.*
import io.vertx.core.eventbus.*
import kotlinx.serialization.*
import kotlinx.serialization.json.*
import java.util.*
import kotlin.reflect.*

class GenericCodec<T>(
  private val serializer: KSerializer<T>,
  private val name: String = UUID.randomUUID().toString()
) : MessageCodec<T, T> {
  companion object {
    @JvmStatic
    fun <T> computationCodec(
      serializer: KSerializer<T>,
      type: KClass<*>
    ): MessageCodec<Computation<T>, Computation<T>> {
      return GenericCodec(Computation.serializer(serializer), computationCodecName(type))
    }

    @JvmStatic
    fun computationCodecName(type: KClass<*>): String {
      return "${Computation::class.simpleName}${type.simpleName}"
    }
  }

  override fun encodeToWire(buffer: Buffer, s: T) {
    buffer.appendString(Json.encodeToString(serializer, s))
  }

  override fun decodeFromWire(pos: Int, buffer: Buffer): T {
    return Json.decodeFromString(serializer, buffer.getString(pos, buffer.length()))
  }

  override fun transform(s: T): T = s

  override fun name(): String = name

  override fun systemCodecID(): Byte = -1
}
