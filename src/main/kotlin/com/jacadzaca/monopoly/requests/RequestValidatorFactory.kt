package com.jacadzaca.monopoly.requests

interface RequestValidatorFactory {
  fun validatorFor(request: Request): RequestValidator
}
