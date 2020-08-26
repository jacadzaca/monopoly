package com.jacadzaca.monopoly.marshallers

interface Marshaller<T, R> {
  fun encode(obj: T): R
  fun decode(raw: R): T
}
