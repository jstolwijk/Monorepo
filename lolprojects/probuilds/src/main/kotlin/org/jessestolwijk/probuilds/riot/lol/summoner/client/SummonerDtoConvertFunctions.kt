package org.jessestolwijk.probuilds.riot.lol.summoner.client

import org.jessestolwijk.probuilds.riot.Region
import org.jessestolwijk.probuilds.riot.lol.summoner.Summoner
import org.jessestolwijk.riot.lol.SummonerV4SummonerDTO

fun SummonerV4SummonerDTO.asSummoner(region: Region) = Summoner(
        name = name,
        region = region,
        puuid = puuid,
        id = id,
        accountId = accountId
)