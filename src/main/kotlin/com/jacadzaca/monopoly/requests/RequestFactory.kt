package com.jacadzaca.monopoly.requests

import java.util.*
import kotlin.reflect.KClass

interface RequestFactory {
  fun <T : Request> instanceOf(playerId: UUID, clazz: KClass<T>): Request
}
