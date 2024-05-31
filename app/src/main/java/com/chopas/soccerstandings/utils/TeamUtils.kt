package com.chopas.soccerstandings.utils

import com.chopas.soccerstandings.model.Games
import com.chopas.soccerstandings.model.Game
import com.chopas.soccerstandings.model.GameResult
import com.chopas.soccerstandings.model.Team
import com.chopas.soccerstandings.model.TeamDetail

class TeamUtils {
    companion object {
        fun parseGamesToTeams(games: Games): List<Team> {
            val teamMap = mutableMapOf<String, Team>()

            games.forEach { game ->
                val homeTeamId = game.homeTeamId
                if (teamMap.containsKey(homeTeamId)) {
                    teamMap[homeTeamId]?.let { homeTeam ->
                        when (getResult(game)) {
                            GameResult.WIN -> homeTeam.winCount++
                            GameResult.LOSE -> homeTeam.lossCount++
                            GameResult.DRAW -> homeTeam.drawCount++
                        }

                        homeTeam.totalGames++
                    }
                } else {
                    teamMap[homeTeamId] = createNewTeam(game, true)
                }

                val awayTeamId = game.awayTeamId
                if (teamMap.containsKey(awayTeamId)) {
                    teamMap[awayTeamId]?.let { awayTeam ->
                        when (getResult(game, false)) {
                            GameResult.WIN -> awayTeam.winCount++
                            GameResult.LOSE -> awayTeam.lossCount++
                            GameResult.DRAW -> awayTeam.drawCount++
                        }

                        awayTeam.totalGames++
                    }
                } else {
                    teamMap[awayTeamId] = createNewTeam(game, false)
                }
            }

            return teamMap.values.toList()
                .sortedWith(compareByDescending<Team> { it.winCount.toBigDecimal() * 100.toBigDecimal() / it.totalGames.toBigDecimal() }
                .thenByDescending { (it.winCount * 3) + it.drawCount })
        }

        private fun getResult(game: Game, home: Boolean = true): GameResult {
            val gameResult = if (game.homeScore - game.awayScore > 0) {
                if (home) GameResult.WIN else GameResult.LOSE
            } else if (game.homeScore - game.awayScore < 0) {
                if (home) GameResult.LOSE else GameResult.WIN
            } else {
                GameResult.DRAW
            }

            return gameResult
        }

        private fun createNewTeam(game: Game, home: Boolean): Team {
            var winCount = 0
            var lossCount = 0
            var drawCount = 0

            when (getResult(game, home)) {
                GameResult.WIN -> winCount++
                GameResult.LOSE -> lossCount++
                GameResult.DRAW -> drawCount++
            }

            return if (home) {
                Team(game.homeTeamId, game.homeTeamName, winCount, lossCount, drawCount, 1)
            } else {
                Team(game.awayTeamId, game.awayTeamName, winCount, lossCount, drawCount, 1)
            }
        }

         fun getWinPercentage(team: Team): String {
            val totalGames = team.winCount + team.lossCount + team.drawCount
            val percentage = (team.winCount.toBigDecimal() * 100.toBigDecimal() / totalGames.toBigDecimal())

            return "$percentage%"
        }

        fun parseTeamToTeamDetail(team: Team, games: Games): TeamDetail {
            val matchUps = mutableMapOf<String, Team>()
            val filteredGames = games.filter { it.awayTeamId == team.teamId || it.homeTeamId == team.teamId }

            filteredGames.forEach { game ->
                val opponentId = if (game.homeTeamId == team.teamId) game.awayTeamId else game.homeTeamId

                if (matchUps.containsKey(opponentId)) {
                    matchUps[opponentId]?.let { opponentTeam ->
                        when (getResult(game, game.homeTeamId == team.teamId)) {
                            GameResult.WIN -> opponentTeam.winCount++
                            GameResult.LOSE -> opponentTeam.lossCount++
                            GameResult.DRAW -> opponentTeam.drawCount++
                        }

                        opponentTeam.totalGames++
                    }
                } else {
                    val opponentTeamName = if (game.homeTeamId == team.teamId) game.awayTeamName else game.homeTeamName
                    var winCount = 0
                    var lossCount = 0
                    var drawCount = 0

                    when (getResult(game, game.homeTeamId == team.teamId)) {
                        GameResult.WIN -> winCount++
                        GameResult.LOSE -> lossCount++
                        GameResult.DRAW -> drawCount++
                    }

                    matchUps[opponentId] = Team(opponentId, opponentTeamName, winCount, lossCount, drawCount, 1)
                }
            }

            return TeamDetail(team.teamId, team.teamName, matchUps)
        }
    }
}