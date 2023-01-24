package icu.fyi.webmon.shared.network

import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

class WebsiteApi {
    private val httpClient = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                useAlternativeNames = false
            })
        }
    }

    suspend fun getApiSys(url: String): Boolean {
        val apiRes: HttpResponse = httpClient.get(url)
        return apiRes.status.value==200
    }

    suspend fun getWww(url: String): Boolean {
        return httpClient.get(url).status.value==200
    }
}