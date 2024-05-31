package com.chopas.soccerstandings.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chopas.soccerstandings.model.Games
import com.chopas.soccerstandings.model.Team
import com.chopas.soccerstandings.model.TeamDetail
import com.chopas.soccerstandings.network.RetrofitInstance
import com.chopas.soccerstandings.utils.TeamUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ResultListViewModel: ViewModel() {
    companion object {
        lateinit var games: Games
    }
    val teamsMutableLiveData = MutableLiveData<List<Team>>()
    val errorLiveData = MutableLiveData<Throwable>()
    val progressBarLiveData = MutableLiveData<Boolean>()

    fun getGameResult() = viewModelScope.launch(Dispatchers.IO) {
        progressBarLiveData.postValue(true)
        val call = RetrofitInstance.api.getSoccerGameResults()
        call.enqueue(object : Callback<Games> {
            override fun onResponse(call: Call<Games>, response: Response<Games>) {
                response.body()?.let {
                    games = it
                    teamsMutableLiveData.postValue(TeamUtils.parseGamesToTeams(it))
                    progressBarLiveData.postValue(false)
                }
            }

            override fun onFailure(call: Call<Games>, t: Throwable) {
                errorLiveData.postValue(t)
            }
        })
    }
}