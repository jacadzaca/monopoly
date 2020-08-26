package com.jacadzaca.monopoly.marshallers

interface Marshaller<in T, out R> {
  fun encode(obj: T): R
}
