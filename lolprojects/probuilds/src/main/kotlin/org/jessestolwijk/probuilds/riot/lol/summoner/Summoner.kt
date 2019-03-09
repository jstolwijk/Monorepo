package org.jessestolwijk.probuilds.riot.lol.summoner

import org.jessestolwijk.probuilds.riot.Region

data class Summoner(
        val id: String,
        val region: Region,
        val name: String,
        val puuid: String,
        val accountId: String
)