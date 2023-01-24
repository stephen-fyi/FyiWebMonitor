package icu.fyi.webmon.shared.dto
@kotlinx.serialization.Serializable
data class LocationReq(val latitude: Double, val longitude: Double, val name: String, val uuid: String)
