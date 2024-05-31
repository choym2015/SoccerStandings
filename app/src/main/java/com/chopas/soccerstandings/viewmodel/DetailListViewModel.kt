package com.chopas.soccerstandings.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.chopas.soccerstandings.model.Team
import com.chopas.soccerstandings.model.TeamDetail
import com.chopas.soccerstandings.utils.TeamUtils

class DetailListViewModel: ViewModel() {
    val teamDetailMutableLiveData = MutableLiveData<TeamDetail>()

    fun getTeamDetails(team: Team) {
        val teamDetail = TeamUtils.parseTeamToTeamDetail(team, ResultListViewModel.games)
        teamDetailMutableLiveData.postValue(teamDetail)
    }
}