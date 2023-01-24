package icu.fyi.webmon.shared.dto

import kotlinx.serialization.Serializable

@Serializable
data class PlayerRes(val players: List<PlayerPosition> = mutableListOf())
@Serializable
data class PlayerPosition(val name: String?, val distance: Double)