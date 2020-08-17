package com.jacadzaca.monopoly.encoders

interface Encoder<in T, out R> {
  fun encode(obj: T): R
}
