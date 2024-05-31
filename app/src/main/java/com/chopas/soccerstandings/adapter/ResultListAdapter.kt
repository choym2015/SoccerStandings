package com.chopas.soccerstandings.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.chopas.soccerstandings.R
import com.chopas.soccerstandings.model.Team
import com.chopas.soccerstandings.utils.TeamUtils

class ResultListAdapter: RecyclerView.Adapter<ResultListAdapter.MyViewHolder>() {
    private var teamList: List<Team>? = null
    var onItemClick: ((Team) -> Unit)? = null

    fun setTeamList(teamList: List<Team>) {
        this.teamList = teamList
    }

    fun getTeamList(): List<Team> {
        return this.teamList!!
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.team_list_row, parent, false)

        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val backgroundColor = if (position % 2 == 0) R.color.grey else R.color.white
        holder.rootView.setBackgroundResource(backgroundColor)
        holder.bind(teamList?.get(position)!!)
    }

    override fun getItemCount(): Int {
        return if (teamList == null) 0 else teamList?.size!!
    }

    inner class MyViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val rootView: LinearLayout = view.findViewById(R.id.rootView)
        private val teamNameTV: TextView = view.findViewById(R.id.teamNameTextView)
        private val numsOfWinTV: TextView = view.findViewById(R.id.numOfWinsTextView)
        private val numsOfLossTV: TextView = view.findViewById(R.id.numOfLossTextView)
        private val numsOfDrawTV: TextView = view.findViewById(R.id.numOfDrawTextView)
        private val winPercentageTV: TextView = view.findViewById(R.id.winPercentageTextView)

        init {
            view.setOnClickListener {
                onItemClick?.invoke(teamList?.get(adapterPosition)!!)
            }
        }

        fun bind(data: Team) {
            teamNameTV.text = data.teamName
            numsOfWinTV.text = data.winCount.toString()
            numsOfLossTV.text = data.lossCount.toString()
            numsOfDrawTV.text = data.drawCount.toString()
            winPercentageTV.text = TeamUtils.getWinPercentage(data)
        }
    }
}