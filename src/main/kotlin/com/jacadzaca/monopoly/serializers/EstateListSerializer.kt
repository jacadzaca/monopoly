package com.jacadzaca.monopoly.serializers

import com.jacadzaca.monopoly.gamelogic.Estate
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.toPersistentList
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

object EstateListSerializer : KSerializer<PersistentList<Estate>> {
    private val listSerializer = ListSerializer(Estate.serializer())

    override fun deserialize(decoder: Decoder): PersistentList<Estate> {
        return listSerializer.deserialize(decoder).toPersistentList()
    }

    override val descriptor: SerialDescriptor = listSerializer.descriptor

    override fun serialize(encoder: Encoder, value: PersistentList<Estate>) {
        listSerializer.serialize(encoder, value)
    }
}
