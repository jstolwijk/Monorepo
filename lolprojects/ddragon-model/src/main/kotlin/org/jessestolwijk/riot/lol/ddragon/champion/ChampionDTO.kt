package org.jessestolwijk.riot.lol.ddragon.champion

data class ChampionDTO(
        val blurb: String,
        val id: String,
        val image: ImageDTO,
        val info: InfoDTO,
        val key: String,
        val name: String,
        val partype: String,
        val stats: StatsDTO,
        val tags: List<String>,
        val title: String,
        val version: String
)