package icu.fyi.webmon.shared.dto

import kotlinx.serialization.Serializable

@Serializable
data class IpRes(val ip:String)

@Serializable
data class ApiRes(val status: Int, val message: String)