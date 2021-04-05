package com.jacadzaca.monopoly.requests

interface ValidatorFactory {
  fun validatorFor(action: PlayerAction): RequestValidator
}
