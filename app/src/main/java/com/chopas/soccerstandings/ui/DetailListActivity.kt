package com.chopas.soccerstandings.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.chopas.soccerstandings.R
import com.chopas.soccerstandings.adapter.DetailListAdapter
import com.chopas.soccerstandings.databinding.ActivityDetailListBinding
import com.chopas.soccerstandings.model.Team
import com.chopas.soccerstandings.viewmodel.DetailListViewModel

class DetailListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailListBinding
    private lateinit var selectedTeam: Team
    private lateinit var detailListAdapter: DetailListAdapter
    private lateinit var detailListViewModel: DetailListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        intent.getParcelableExtra<Team>(getString(R.string.team_payload))?.let {
            selectedTeam = it
        }

        detailListViewModel = ViewModelProvider(this)[DetailListViewModel::class.java]

        setToolBar()
        initRecyclerView()
        setViewModelListeners()
    }

    private fun setToolBar() {
        setSupportActionBar(binding.toolBar)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            setDisplayShowTitleEnabled(true)
            title = selectedTeam.teamName
        }
    }

    private fun initRecyclerView() {
        binding.teamRecyclerView.layoutManager = LinearLayoutManager(this)
        detailListAdapter = DetailListAdapter()
        binding.teamRecyclerView.adapter = detailListAdapter
        binding.teamRecyclerView.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))
    }

    private fun setViewModelListeners() {
        detailListViewModel.teamDetailMutableLiveData.observe(this) { teamDetail ->
            val teams = teamDetail.matchUps.values.toList().sortedByDescending { it.winCount }
            detailListAdapter.setTeamList(teams)
            detailListAdapter.notifyDataSetChanged()
        }

        detailListViewModel.getTeamDetails(selectedTeam)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}