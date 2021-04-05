package com.jacadzaca.monopoly.requests.validators

import com.jacadzaca.monopoly.requests.*

interface ValidatorFactory {
  fun validatorFor(action: PlayerAction): RequestValidator
}
