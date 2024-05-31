package com.chopas.soccerstandings.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TeamDetail(
    val teamId: String,
    val teamName: String,
    var matchUps: MutableMap<String, Team>
): Parcelable
