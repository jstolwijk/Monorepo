package org.jessestolwijk.riot.lol.ddragon.champion

data class ChampionListDTO(
        val data: Map<String, ChampionDTO>,
        val format: String,
        val type: String,
        val version: String
)