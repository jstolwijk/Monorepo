package org.jessestolwijk.probuilds.riot

import io.ktor.client.HttpClient
import io.ktor.client.engine.apache.Apache
import io.ktor.client.features.UserAgent
import io.ktor.client.features.defaultRequest
import io.ktor.client.features.json.JacksonSerializer
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.request.header

val httpClient by lazy {
    HttpClient(Apache) {
        install(JsonFeature) {
            serializer = JacksonSerializer()
        }
        install(UserAgent) {
            agent = "ktor"
        }
    }
}

class RiotClient(private val host: String = "api.riotgames.com",
                 private val riotToken: String = "RGAPI-2052fc60-4ef4-4727-b209-877b78cc13c5") {

    val riotClient: HttpClient by lazy {
        httpClient.config {
            defaultRequest {
                header("X-Riot-Token", riotToken)
            }
        }
    }

    val Region.baseUrl: String
        get() {
            val platform = servicePlatforms.first().toLowerCase()
            return "https://$platform.$host"
        }
}

