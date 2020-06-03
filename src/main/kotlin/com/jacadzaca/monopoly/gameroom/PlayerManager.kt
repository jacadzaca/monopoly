package com.jacadzaca.monopoly.gameroom

import com.jacadzaca.monopoly.gamelogic.player.Player
import io.reactivex.Completable
import io.reactivex.Maybe
import java.util.UUID

interface PlayerManager {
  fun savePlayer(player: Player): Completable
  fun getPlayer(id: UUID): Maybe<Player>
}
