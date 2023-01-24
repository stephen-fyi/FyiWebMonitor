package icu.fyi.webmon.shared.network

import icu.fyi.webmon.shared.dto.LocationReq
import icu.fyi.webmon.shared.dto.PlayerPosition
import icu.fyi.webmon.shared.dto.PlayerRes
import icu.fyi.webmon.shared.entity.UserEntry
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

class LocationApi {
    private val httpClient = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                useAlternativeNames = false
            })
        }
    }

    suspend fun sendLocation(latitude: Double, longitude: Double, name: String, uuid: String): List<PlayerPosition> {
        println("Send Location")
        val playerRes: HttpResponse = httpClient.post("https://fyi.icu/api/location") {
            contentType(ContentType.Application.Json)
            setBody(LocationReq(latitude=latitude, longitude=longitude, name=name, uuid=uuid))
        }
        playerRes.call
            //.post("https://fyi.icu/location")
            //.body(LocationReq(latitude=latitude, longitude=longitude))
        println("Location Res Status ${playerRes.status}")

        val playersRes = playerRes.body<PlayerRes>()
        playersRes.players.forEach { player->
            println("player ${player.name} distance ${player.distance}")
        }
        return playersRes.players
    }

    suspend fun createUser(): UserEntry {
        val res = httpClient.get("https://fyi.icu/api/player")
        return res.body()
    }
}