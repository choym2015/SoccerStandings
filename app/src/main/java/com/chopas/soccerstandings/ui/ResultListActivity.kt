package com.chopas.soccerstandings.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.chopas.soccerstandings.R
import com.chopas.soccerstandings.adapter.ResultListAdapter
import com.chopas.soccerstandings.databinding.ActivityResultListBinding
import com.chopas.soccerstandings.viewmodel.ResultListViewModel
import com.google.android.material.snackbar.Snackbar


class ResultListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultListBinding
    private lateinit var resultListViewModel: ResultListViewModel
    private lateinit var resultListAdapter: ResultListAdapter
    private lateinit var filters: List<TextView>
    private var selectedFilterIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityResultListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        resultListViewModel = ViewModelProvider(this)[ResultListViewModel::class.java]

        initRecyclerView()
        setViewModelListeners()
        setFilterOnClicks()
        loadGameResult()
    }

    private fun initRecyclerView() {
        binding.resultListRecyclerView.layoutManager = LinearLayoutManager(this)
        resultListAdapter = ResultListAdapter()
        binding.resultListRecyclerView.adapter = resultListAdapter
        binding.resultListRecyclerView.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))
        resultListAdapter.onItemClick = { team ->
            val intent = Intent(this, DetailListActivity::class.java)
            intent.putExtra(getString(R.string.team_payload), team)
            startActivity(intent)
        }
    }

    private fun setViewModelListeners() {
        resultListViewModel.teamsMutableLiveData.observe(this) { teams ->
            resultListAdapter.setTeamList(teams)
            resultListAdapter.notifyDataSetChanged()
        }

        resultListViewModel.errorLiveData.observe(this) { error ->
            val snackBar = Snackbar.make(binding.root, error.localizedMessage, Snackbar.LENGTH_SHORT)
            snackBar.show()
        }

        resultListViewModel.progressBarLiveData.observe(this) { isLoading ->
            binding.loadingLinearLayout.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }

    private fun loadGameResult() {
        resultListViewModel.getGameResult()
    }

    private fun setFilterOnClicks() {
        filters = listOf(binding.headerView.teamNameTextView, binding.headerView.numOfWinsTextView,
            binding.headerView.numOfLossTextView, binding.headerView.numOfDrawTextView, binding.headerView.winPercentageTextView)
        selectedFilterIndex = 4

        binding.headerView.teamNameTextView.setOnClickListener {
            val nameSortedList = resultListAdapter.getTeamList().sortedBy { it.teamName }
            resultListAdapter.setTeamList(nameSortedList)
            resultListAdapter.notifyDataSetChanged()

            updateFilterColor(0)
        }

        binding.headerView.numOfWinsTextView.setOnClickListener {
            val winSortedTeamList = resultListAdapter.getTeamList().sortedByDescending { it.winCount }
            resultListAdapter.setTeamList(winSortedTeamList)
            resultListAdapter.notifyDataSetChanged()

            updateFilterColor(1)
        }

        binding.headerView.numOfLossTextView.setOnClickListener {
            val lossSortedTeamList = resultListAdapter.getTeamList().sortedByDescending { it.lossCount }
            resultListAdapter.setTeamList(lossSortedTeamList)
            resultListAdapter.notifyDataSetChanged()

            updateFilterColor(2)
        }

        binding.headerView.numOfDrawTextView.setOnClickListener {
            val drawSortedTeamList = resultListAdapter.getTeamList().sortedByDescending { it.drawCount }
            resultListAdapter.setTeamList(drawSortedTeamList)
            resultListAdapter.notifyDataSetChanged()

            updateFilterColor(3)
        }

        binding.headerView.winPercentageTextView.setOnClickListener {
            val winPercentageSortedTeamList = resultListAdapter.getTeamList().sortedByDescending {
                it.winCount.toBigDecimal() * 100.toBigDecimal() / it.totalGames.toBigDecimal()
            }
            resultListAdapter.setTeamList(winPercentageSortedTeamList)
            resultListAdapter.notifyDataSetChanged()

            updateFilterColor(4)
        }
    }

    private fun updateFilterColor(newIndex: Int) {
        val color = if (selectedFilterIndex == 0) getColor(R.color.black) else getColor(androidx.appcompat.R.color.secondary_text_default_material_light)
        filters[selectedFilterIndex].setTextColor(color)
        selectedFilterIndex = newIndex
        filters[selectedFilterIndex].setTextColor(getColor(R.color.blue))
    }
}
