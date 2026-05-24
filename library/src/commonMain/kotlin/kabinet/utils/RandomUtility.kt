package kabinet.utils

import kotlin.random.Random

fun Random.diceRoll(sides: Int) = nextInt(1, sides + 1)
