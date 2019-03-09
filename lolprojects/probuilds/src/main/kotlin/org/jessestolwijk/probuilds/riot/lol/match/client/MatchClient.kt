package org.jessestolwijk.probuilds.riot.lol.match.client

import io.ktor.client.request.get
import org.jessestolwijk.probuilds.riot.Region
import org.jessestolwijk.probuilds.riot.RiotClient
import org.jessestolwijk.probuilds.riot.lol.summoner.Summoner
import org.jessestolwijk.riot.lol.MatchV4MatchDto
import org.jessestolwijk.riot.lol.MatchV4MatchlistDto

private const val MATCH_API_V4 = "/lol/match/v4"

suspend fun RiotClient.getMatchListBySummoner(summoner: Summoner) =
        riotClient.get<MatchV4MatchlistDto>("${summoner.region.baseUrl}/$MATCH_API_V4/matchlists/by-account/${summoner.accountId}")

suspend fun RiotClient.getMatchByMatchId(region: Region, matchId: Long) =
        riotClient.get<MatchV4MatchDto>("${region.baseUrl}/$MATCH_API_V4/matches/$matchId")
