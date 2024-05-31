package com.chopas.soccerstandings.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Team(
    val teamId: String,
    val teamName: String,
    var winCount: Int = 0,
    var lossCount: Int = 0,
    var drawCount: Int = 0,
    var totalGames: Int = 0
): Parcelable