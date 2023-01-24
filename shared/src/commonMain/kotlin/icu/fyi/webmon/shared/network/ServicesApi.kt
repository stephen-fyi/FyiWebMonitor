package icu.fyi.webmon.shared.network

import icu.fyi.webmon.shared.dto.IpRes
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

class ServicesApi {
    private val httpClient = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                useAlternativeNames = false
            })
        }
    }

    suspend fun getIp(): String {
        val ipRes: IpRes = httpClient.get("https://ip.fyi.icu").body()
        println("ip ${ipRes.ip}")
        return ipRes.ip
    }
}