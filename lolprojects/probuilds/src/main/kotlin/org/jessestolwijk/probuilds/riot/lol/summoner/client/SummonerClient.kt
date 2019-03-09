package org.jessestolwijk.probuilds.riot.lol.summoner.client

import io.ktor.client.request.get
import mu.KotlinLogging
import org.jessestolwijk.probuilds.riot.Region
import org.jessestolwijk.probuilds.riot.RiotClient
import org.jessestolwijk.riot.lol.SummonerV4SummonerDTO

private val log = KotlinLogging.logger { }

private const val SUMMONER_API_V4 = "/lol/summoner/v4/summoners"

private suspend fun RiotClient.getSummoner(region: Region, by: String, subject: String) =
        riotClient.get<SummonerV4SummonerDTO>("${region.baseUrl}/$SUMMONER_API_V4/$by/$subject").asSummoner(region)

suspend fun RiotClient.getSummonerByName(region: Region, summonerName: String) =
        getSummoner(region, "by-name", summonerName)

suspend fun RiotClient.getSummonerByAccountId(region: Region, accountId: String) =
        getSummoner(region, "by-account", accountId)

suspend fun RiotClient.getSummonerByPuuid(region: Region, puuid: String) =
        getSummoner(region, "by-puuid", puuid)
