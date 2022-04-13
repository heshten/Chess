package com.heshten.core.models.markers

interface TakeMarker {

  fun canTakeVertically(): Boolean

  fun canTakeHorizontally(): Boolean

  fun canTakeDiagonally(): Boolean

  fun canTakeKnightLike(): Boolean

  fun canTakeBehind(): Boolean

  fun maxTakeSteps(): Int
}
