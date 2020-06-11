package com.kavics.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kavics.R
import com.kavics.model.DeadlineItem
import com.kavics.model.Item
import com.kavics.model.KavicItem
import kotlinx.android.synthetic.main.deadline_item.view.*
import kotlinx.android.synthetic.main.kavic_cardview.view.*
import java.util.*
import kotlin.concurrent.schedule

class SimpleItemRecyclerViewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var timer: TimerTask
    private val kavicList = mutableListOf<Item>()
    var itemClickListener: KavicItemClickListener? = null
    private val dateHelper = DeadlineHelper()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == 1) {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.kavic_cardview, parent, false)
            KavicCardViewViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.deadline_item, parent, false)
            DeadlineDateViewViewHolder(view)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (kavicList[position] is KavicItem) {
            1
        } else {
            0
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val kavic = kavicList[position]

        if (holder is KavicCardViewViewHolder) {
            holder.kavicItem = kavic as KavicItem
            holder.tvTitle.text = kavic.title
        } else if (holder is DeadlineDateViewViewHolder) {

            holder.kavicItem = kavic as DeadlineItem

            when (kavic.deadline) {
                dateHelper.getToday() -> {
                    holder.setBackgroundColor(Color.RED)
                    holder.tvDeadlineDate.text = "Today"
                }
                dateHelper.getTomorrow() -> {
                    holder.setBackgroundColor(Color.YELLOW)
                    holder.tvDeadlineDate.text = "Tomorrow"
                }
                else -> {
                    holder.setBackgroundColor(Color.GREEN)
                    holder.tvDeadlineDate.text = kavic.deadline
                }
            }
        }
    }

    fun addAll(kavicItems: List<KavicItem>) {
        val kavicsAndDates = dateHelper.getListWithDates(kavicItems)
        kavicList.clear()
        kavicList.addAll(kavicsAndDates)
        notifyDataSetChanged()
    }

    override fun getItemCount() = kavicList.size

    inner class KavicCardViewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val tvTitle: TextView = itemView.textViewKavicItemTitle
        var kavicItem: KavicItem? = null
        private val checkBoxDone: CheckBox = itemView.checkBoxDone

        init {
            itemView.setOnClickListener {
                kavicItem?.let { kavic -> itemClickListener?.onItemClick(kavic) }
            }

            itemView.setOnLongClickListener { view ->
                kavicItem?.let { itemClickListener?.onItemLongClick(adapterPosition, view, it) }
                true
            }

            checkBoxDone.setOnClickListener {
                if (checkBoxDone.isChecked) {
                    timer = Timer("Navigate to next activity", false).schedule(1000) {
                        kavicItem?.let { kavic -> itemClickListener?.checkBoxChecked(kavic) }
                        timer.cancel()
                    }
                }
            }

        }
    }

    inner class DeadlineDateViewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val tvDeadlineDate: TextView = itemView.textViewDateName
        var kavicItem: Item? = null

        fun setBackgroundColor(color: Int) {
            itemView.setBackgroundColor(color)
        }

    }
}