package org.jessestolwijk.probuilds.riot

enum class Region(val servicePlatforms: List<String>){
    BR(listOf("BR1")),
    EUNE(listOf("EUN1")),
    EUW(listOf("EUW1")),
    JP(listOf("JP1")),
    KR(listOf("KR")),
    LAN(listOf("LA1")),
    LAS(listOf("LA2")),
    NA(listOf("NA1", "NA")),
    OCE(listOf("OC1")),
    TR(listOf("BR1")),
    RU(listOf("RU")),
    PBE(listOf("PBE1"))
}