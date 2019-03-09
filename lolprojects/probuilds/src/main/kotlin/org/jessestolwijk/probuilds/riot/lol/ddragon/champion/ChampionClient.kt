package org.jessestolwijk.probuilds.riot.lol.ddragon.champion

import io.ktor.client.request.get
import org.jessestolwijk.probuilds.riot.httpClient
import org.jessestolwijk.riot.lol.ddragon.champion.ChampionListDTO

object ChampionClient {

    suspend fun getChampions() =
            httpClient.get<ChampionListDTO>("http://ddragon.leagueoflegends.com/cdn/6.24.1/data/en_US/champion.json")
}