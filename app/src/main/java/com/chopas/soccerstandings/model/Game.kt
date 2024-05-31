package com.chopas.soccerstandings.model

data class Game(
    val awayScore: Int,
    val awayTeamId: String,
    val awayTeamName: String,
    val gameId: String,
    val homeScore: Int,
    val homeTeamId: String,
    val homeTeamName: String
)