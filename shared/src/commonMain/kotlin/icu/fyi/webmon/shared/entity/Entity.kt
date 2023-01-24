package icu.fyi.webmon.shared.entity

import kotlinx.serialization.Serializable
import java.util.UUID
import java.time.LocalDateTime

@Serializable
data class WebsiteEntry(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val url: String,
    var live: Boolean? = false,
    val type: String,
    var checkedAt: String
)
@Serializable
data class UserEntry(
    val uuid: String,
    val name: String,
    val email: String,
    val latitude: Double?,
    val longitude: Double?
)