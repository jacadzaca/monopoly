package com.jacadzaca.monopoly.gamelogic

import com.jacadzaca.monopoly.gamelogic.buildings.Building
import java.util.*

data class Tile(val buildings: List<Building>, val owner: UUID)
