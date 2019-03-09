package org.jessestolwijk.probuilds

import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.CallLogging
import io.ktor.features.ContentNegotiation
import io.ktor.jackson.jackson
import io.ktor.response.respond
import io.ktor.routing.Routing
import io.ktor.routing.get
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import org.jessestolwijk.probuilds.riot.Region
import org.jessestolwijk.probuilds.riot.RiotClient
import org.jessestolwijk.probuilds.riot.lol.ddragon.champion.ChampionClient
import org.jessestolwijk.probuilds.riot.lol.match.client.getMatchByMatchId
import org.jessestolwijk.probuilds.riot.lol.match.client.getMatchListBySummoner
import org.jessestolwijk.probuilds.riot.lol.summoner.client.getSummonerByName

fun Application.main() {
    install(CallLogging)

    install(ContentNegotiation) {
        jackson {
            writerWithDefaultPrettyPrinter()
        }
    }

    val riotClient = RiotClient()

    install(Routing) {
        get("/") {
            val summoner = riotClient.getSummonerByName(Region.EUW, "FrankAugurk")
            call.respond(summoner)
        }
        get("/match") {
            val summoner = riotClient.getSummonerByName(Region.NA, "JG IS RUINED")
            val matches = riotClient.getMatchListBySummoner(summoner)
            call.respond(matches)
        }

        get("/c") {
            val champions = ChampionClient.getChampions()
            call.respond(champions)
        }
        get("/matchd") {
            val summoner = riotClient.getSummonerByName(Region.NA, "JG IS RUINED")
            val matches = riotClient.getMatchListBySummoner(summoner)

            val result = matches.matches.take(15).map { match ->
                async {
                    val matchDetails = riotClient.getMatchByMatchId(Region.NA, match.gameId)

                    val participantIdentity = matchDetails.participantIdentities.single {
                        it.player.summonerId == summoner.id
                    }

                    val participant = matchDetails.participants.single {
                        it.participantId == participantIdentity.participantId
                    }

                    Response(participant.championId, participant.stats.isWin)
                }
            }.awaitAll()

            call.respond(result)
        }
    }
}

data class Response(
        val champion: Int,
        val win: Boolean
)
