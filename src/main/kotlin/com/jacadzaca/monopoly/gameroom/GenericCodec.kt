package com.jacadzaca.monopoly.gameroom

import com.jacadzaca.monopoly.Computation
import io.vertx.core.buffer.Buffer
import io.vertx.core.eventbus.MessageCodec
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import java.util.UUID
import kotlin.reflect.KClass

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
